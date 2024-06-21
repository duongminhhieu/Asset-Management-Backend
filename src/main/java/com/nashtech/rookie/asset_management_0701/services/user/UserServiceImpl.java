package com.nashtech.rookie.asset_management_0701.services.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.constants.DefaultSortOptions;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.ChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.FirstChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserSearchDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.UserMapper;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    @Override
    public UserResponse createUser (UserRequest userRequest) {

        String adminName = authUtil.getCurrentUserName();

        validateJoinDate(userRequest);
        User user = createUserFromRequest(userRequest, adminName);

        // Save user to get the actual ID then update correct staff code
        user = userRepository.save(user);
        user.setStaffCode(String.format("SD%04d", user.getId()));
        user = userRepository.save(user);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    private void validateJoinDate (UserRequest userRequest) {
        LocalDate dob = userRequest.getDob();
        LocalDate joinDate = userRequest.getJoinDate();

        if (joinDate.isBefore(dob)) {
            throw new AppException(ErrorCode.JOIN_DATE_BEFORE_DOB);
        }

        DayOfWeek dayOfWeek = joinDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new AppException(ErrorCode.JOIN_DATE_WEEKEND);
        }
    }

    private StringBuilder generateUsername (UserRequest userRequest) {
        StringBuilder username = new StringBuilder(userRequest.getFirstName().toLowerCase());
        String[] splitSpace = userRequest.getLastName().toLowerCase().split(" ");

        for (String s : splitSpace) {
            username.append(s.charAt(0));
        }

        long count = userRepository.count(UserSpecification.usernameStartsWith(username.toString()));
        if (count > 0) {
            username.append(count);
        }

        return username;
    }

    private User createUserFromRequest (UserRequest userRequest, String adminName) {
        String username = generateUsername(userRequest).toString();
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        if (userRequest.getRole().equals(ERole.USER)) {
            User admin = userRepository
                    .findByUsername(adminName)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            userRequest.setLocationId(admin.getLocation().getId());
        }

        User user = userMapper.toUser(userRequest);
        user.setId(userRepository.count() + 1);
        user.setStaffCode("SD0000");
        user.setUsername(username);
        user.setHashPassword(passwordEncoder.encode(username + "@" + newFormatter.format(userRequest.getDob())));
        user.setStatus(EUserStatus.FIRST_LOGIN);

        return user;
    }

    @Override
    public PaginationResponse<UserResponse> getAllUser (UserSearchDto dto) {
        var pageRequest = PageSortUtil.createPageRequest(
                    dto.getPageNumber() - 1,
                    dto.getPageSize(),
                    dto.getOrderBy().equals("type")?"role":dto.getOrderBy(),
                    PageSortUtil.parseSortDirection(dto.getSortDir()),
                    DefaultSortOptions.DEFAULT_USER_SORT_BY);

        var searchString = dto.getSearchString();
        var currentUser = authUtil.getCurrentUser();
        var currentLocation = currentUser.getLocation();

        var users = userRepository.findAll(
                        Specification.where(UserSpecification.hasNameContains(searchString))
                                .or(UserSpecification.hasStaffCodeContains(searchString))
                                .and(UserSpecification.hasRole(dto.getType()))
                                .and(UserSpecification.hasLocation(currentLocation))
                                .and(UserSpecification.excludeUser(currentUser)),
                        pageRequest);

        return PaginationResponse.<UserResponse>builder()
                    .page(pageRequest.getPageNumber() + 1)
                    .total(users.getTotalElements())
                    .itemsPerPage(pageRequest.getPageSize())
                    .data(users.map(userMapper::toUserResponse).toList())
                    .build();
    }

    @Override
    @Transactional
    public void firstChangePassword (FirstChangePasswordRequest firstChangePasswordRequest) {
        User user = authUtil.getCurrentUser();

        if (!user.getStatus().equals(EUserStatus.FIRST_LOGIN)){
            throw new AppException(ErrorCode.PASSWORD_CHANGED);
        }

        user.setHashPassword(passwordEncoder.encode(firstChangePasswordRequest.getPassword()));

        user.setStatus(EUserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword (ChangePasswordRequest changePasswordRequest) {
        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_SAME);
        }
        User user = authUtil.getCurrentUser();
        if (!passwordEncoder.matches(changePasswordRequest.getPassword(), user.getHashPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        user.setHashPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }
}

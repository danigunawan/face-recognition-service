/*
 * Copyright (c) 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.exadel.frs.controller;

import static com.exadel.frs.system.global.Constants.GUID_EXAMPLE;
import com.exadel.frs.dto.ui.AppCreateDto;
import com.exadel.frs.dto.ui.AppResponseDto;
import com.exadel.frs.dto.ui.AppUpdateDto;
import com.exadel.frs.dto.ui.ModelShareResponseDto;
import com.exadel.frs.dto.ui.UserInviteDto;
import com.exadel.frs.dto.ui.UserRoleResponseDto;
import com.exadel.frs.dto.ui.UserRoleUpdateDto;
import com.exadel.frs.enums.AppRole;
import com.exadel.frs.helpers.SecurityUtils;
import com.exadel.frs.mapper.AppMapper;
import com.exadel.frs.mapper.UserAppRoleMapper;
import com.exadel.frs.service.AppService;
import com.exadel.frs.system.statistics.CallStatistics;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/org/{orgGuid}")
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;
    private final AppMapper appMapper;
    private final UserAppRoleMapper userAppRoleMapper;

    @CallStatistics
    @GetMapping("/app/{guid}")
    @ApiOperation(value = "Get application")
    public AppResponseDto getApp(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application to return", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        return appMapper.toResponseDto(
                appService.getApp(orgGuid, guid, SecurityUtils.getPrincipalId()), SecurityUtils.getPrincipalId()
        );
    }

    @GetMapping("/apps")
    @ApiOperation(value = "Get all applications in organization")
    public List<AppResponseDto> getApps(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid
    ) {
        return appMapper.toResponseDto(appService.getApps(orgGuid, SecurityUtils.getPrincipalId()), SecurityUtils.getPrincipalId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/app")
    @ApiOperation(value = "Create application")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Field name cannot be empty")
    })
    public AppResponseDto createApp(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "Application object that needs to be created", required = true)
            @Valid
            @RequestBody
            final AppCreateDto appCreateDto
    ) {
        return appMapper.toResponseDto(appService.createApp(appCreateDto, orgGuid, SecurityUtils.getPrincipalId()), SecurityUtils.getPrincipalId());
    }

    @PutMapping("/app/{guid}")
    @ApiOperation(value = "Update application")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Field name cannot be empty")
    })
    public AppResponseDto updateApp(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application that needs to be updated", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "Application data", required = true)
            @Valid
            @RequestBody
            final AppUpdateDto appUpdateDto
    ) {
        val userId = SecurityUtils.getPrincipalId();
        val updatedApplication = appService.updateApp(appUpdateDto, orgGuid, guid, userId);

        return appMapper.toResponseDto(updatedApplication, userId);
    }

    @PutMapping("/app/{guid}/apikey")
    @ApiOperation(value = "Generate new api-key for application")
    public AppResponseDto regenerateApiKey(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of the application which api-key needs to be regenerated", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        appService.regenerateApiKey(orgGuid, guid, SecurityUtils.getPrincipalId());

        return appMapper.toResponseDto(appService.getApp(orgGuid, guid, SecurityUtils.getPrincipalId()), SecurityUtils.getPrincipalId());
    }

    @DeleteMapping("/app/{guid}")
    @ApiOperation(value = "Delete application")
    public void deleteApp(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of the application that needs to be deleted", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        appService.deleteApp(orgGuid, guid, SecurityUtils.getPrincipalId());
    }

    @GetMapping("/app/{guid}/assign-roles")
    @ApiOperation(value = "Get application roles, that can be assigned to other users")
    public AppRole[] getOrgRolesToAssign(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of the application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        return appService.getAppRolesToAssign(orgGuid, guid, SecurityUtils.getPrincipalId());
    }

    @GetMapping("/app/{guid}/roles")
    @ApiOperation(value = "Get users of application")
    public List<UserRoleResponseDto> getAppUsers(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "Filter by first name, last name or email of user")
            @RequestParam(required = false)
            final String search
    ) {
        return userAppRoleMapper.toUserRoleResponseDto(appService.getAppUsers(search, orgGuid, guid, SecurityUtils.getPrincipalId()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/app/{guid}/invite")
    @ApiOperation(value = "Invite user to application")
    public UserRoleResponseDto inviteUser(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "Application object that needs to be created", required = true)
            @Valid
            @RequestBody
            final UserInviteDto userInviteDto
    ) {
        return userAppRoleMapper.toUserRoleResponseDto(appService.inviteUser(userInviteDto, orgGuid, guid, SecurityUtils.getPrincipalId()));
    }

    @PutMapping("/app/{guid}/role")
    @ApiOperation(value = "Update user application role")
    public UserRoleResponseDto updateUserAppRole(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "User role data", required = true)
            @Valid
            @RequestBody
            final UserRoleUpdateDto userRoleUpdateDto
    ) {
        val userAppRole = appService.updateUserAppRole(userRoleUpdateDto, orgGuid, guid, SecurityUtils.getPrincipalId());

        return userAppRoleMapper.toUserRoleResponseDto(userAppRole);
    }

    @GetMapping("/app/{guid}/model/request")
    @ApiOperation("Request for the model to be shared.")
    public ModelShareResponseDto modelShareRequest(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        val requestId = appService.generateUuidToRequestModelShare(orgGuid, guid);

        return ModelShareResponseDto.builder()
                                .modelRequestUuid(requestId)
                                .build();
    }

    @DeleteMapping("/app/{guid}/user/{userGuid}")
    @ApiOperation(value = "Delete user from application")
    public void deleteUserApp(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of the application that needs to be deleted", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "User guid for deleting from organization", required = true)
            @PathVariable
            final String userGuid
    ) {
        appService.deleteUserFromApp(userGuid, orgGuid, guid, SecurityUtils.getPrincipalId());
    }
}
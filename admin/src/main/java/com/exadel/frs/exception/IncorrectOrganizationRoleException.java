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

package com.exadel.frs.exception;

import static com.exadel.frs.handler.ExceptionCode.INCORRECT_ORGANIZATION_ROLE;
import static java.lang.String.format;

public class IncorrectOrganizationRoleException extends BasicException {

    public static final String ORGANIZATION_ROLE_NOT_EXISTS_MESSAGE = "Organization role %s does not exists";

    public IncorrectOrganizationRoleException(final String organizationRole) {
        super(INCORRECT_ORGANIZATION_ROLE, format(ORGANIZATION_ROLE_NOT_EXISTS_MESSAGE, organizationRole));
    }

}

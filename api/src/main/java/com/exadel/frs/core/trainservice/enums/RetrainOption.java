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

package com.exadel.frs.core.trainservice.enums;

import static com.google.common.base.Enums.getIfPresent;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import com.exadel.frs.core.trainservice.exception.ClassifierIsAlreadyTrainingException;
import com.exadel.frs.core.trainservice.service.RetrainService;

public enum RetrainOption {

    YES {
        @Override
        public void run(final String apiKey, final RetrainService retrainService) {
            if (retrainService.isTrainingRun(apiKey)) {
                throw new ClassifierIsAlreadyTrainingException();
            }

            retrainService.startRetrain(apiKey);
        }
    },
    NO {
        @Override
        public void run(final String apiKey, final RetrainService retrainService) {
            //This option requires no training
        }
    },
    FORCE {
        @Override
        public void run(final String apiKey, final RetrainService retrainService) {
            retrainService.abortTraining(apiKey);
            retrainService.startRetrain(apiKey);
        }
    };

    public abstract void run(final String apiKey, final RetrainService retrainService);

    public static RetrainOption getTrainingOption(final String option) {
        return getIfPresent(RetrainOption.class, firstNonNull(option, "").toUpperCase())
                .or(FORCE);
    }
}
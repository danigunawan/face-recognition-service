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

package com.exadel.frs.core.trainservice.service;

import static com.exadel.frs.core.trainservice.enums.RetrainOption.getTrainingOption;
import com.exadel.frs.core.trainservice.component.FaceClassifierManager;
import com.exadel.frs.core.trainservice.dao.FaceDao;
import com.exadel.frs.core.trainservice.entity.mongo.Face;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceService {

    private final FaceDao faceDao;
    private final RetrainService retrainService;
    private final FaceClassifierManager classifierManager;

    public List<Face> findFaces(final String apiKey) {
        return faceDao.findAllFacesByApiKey(apiKey);
    }

    public void deleteFaceByName(
            final String faceName,
            final String apiKey,
            final String retrain
    ) {
        faceDao.deleteFaceByName(faceName, apiKey);
        getTrainingOption(retrain).run(apiKey, retrainService);
    }

    public void deleteFaceById(
            final String faceId,
            final String apiKey,
            final String retrain
    ) {
        faceDao.deleteFaceById(faceId);
        getTrainingOption(retrain).run(apiKey, retrainService);
    }

    public int deleteFacesByModel(final String modelKey) {
        classifierManager.removeFaceClassifier(modelKey);
        val deletedFaces = faceDao.deleteFacesByApiKey(modelKey);

        return deletedFaces.size();
    }
}
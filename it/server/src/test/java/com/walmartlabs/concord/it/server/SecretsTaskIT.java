package com.walmartlabs.concord.it.server;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.walmartlabs.concord.client.ProcessApi;
import com.walmartlabs.concord.client.ProcessEntry;
import com.walmartlabs.concord.client.StartProcessResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.walmartlabs.concord.it.common.ITUtils.archive;
import static com.walmartlabs.concord.it.common.ServerClient.assertLog;
import static com.walmartlabs.concord.it.common.ServerClient.waitForCompletion;
import static org.junit.Assert.assertNotNull;

public class SecretsTaskIT extends AbstractServerIT {

    @Test(timeout = DEFAULT_TEST_TIMEOUT)
    public void test() throws Exception {
        String secretName = "secret_" + randomString();

        byte[] payload = archive(SecretsTaskIT.class.getResource("secretsTask").toURI());

        Map<String, Object> input = new HashMap<>();
        input.put("archive", payload);
        input.put("arguments.secretName", secretName);

        StartProcessResponse spr = start(input);

        ProcessApi processApi = new ProcessApi(getApiClient());
        ProcessEntry pir = waitForCompletion(processApi, spr.getInstanceId());
        assertNotNull(pir.getLogFileName());

        byte[] bytes = getLog(pir.getLogFileName());
        // System.out.println(new String(bytes));
        assertLog(".* Delete secret2.*", bytes);
    }
}

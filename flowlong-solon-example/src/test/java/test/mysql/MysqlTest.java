/* Copyright 2023-2025 jobob@qq.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.mysql;

import com.flowlong.bpm.engine.QueryService;
import com.flowlong.bpm.engine.core.FlowCreator;
import com.flowlong.bpm.engine.entity.FlwTask;
import com.flowlong.bpm.engine.entity.FlwTaskActor;
import com.flowlong.bpm.solon.example.FlowLongApp;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;
import test.TestFlowLong;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Mysql 测试基类
 */
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest(FlowLongApp.class)
public class MysqlTest extends TestFlowLong {

    protected FlowCreator testCreator = FlowCreator.of(testUser1, "测试001");
    protected FlowCreator test3Creator = FlowCreator.of(testUser3, "测试003");

    /**
     * 执行当前活跃用户
     *
     * @param instanceId  流程实例ID
     * @param testCreator 任务创建者
     */
    public void executeActiveTasks(Long instanceId, FlowCreator testCreator) {
        this.executeActiveTasks(instanceId, t -> this.flowLongEngine.executeTask(t.getId(), testCreator));
    }

    public void executeActiveTasks(Long instanceId, Consumer<FlwTask> taskConsumer) {
        this.flowLongEngine.queryService().getActiveTasksByInstanceId(instanceId)
                .ifPresent(tasks -> tasks.forEach(t -> taskConsumer.accept(t)));
    }

    public void executeTask(Long instanceId, FlowCreator flowCreator) {
        QueryService queryService = this.flowLongEngine.queryService();
        List<FlwTask> flwTaskList = queryService.getTasksByInstanceId(instanceId);
        for (FlwTask flwTask : flwTaskList) {
            List<FlwTaskActor> taskActors = queryService.getTaskActorsByTaskId(flwTask.getId());
            if (null != taskActors && taskActors.stream()
                    // 找到当前对应审批的任务执行
                    .anyMatch(t -> Objects.equals(t.getActorId(), flowCreator.getCreateId()))) {
                // 执行审批
                this.flowLongEngine.executeTask(flwTask.getId(), flowCreator);
            }
        }
    }
}

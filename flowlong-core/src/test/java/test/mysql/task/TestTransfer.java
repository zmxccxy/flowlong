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
package test.mysql.task;

import com.flowlong.bpm.engine.entity.Instance;
import com.flowlong.bpm.engine.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.mysql.MysqlTest;

import java.util.List;

/**
 * @Description: TestTransfer
 * @Author: Binfeng.Yan
 * @Date: 2023/3/4 16:26
 * @Version: 1.0
 */
public class TestTransfer extends MysqlTest {

	@BeforeEach
	public void before() {
		processId = this.deployByResource("test/task/transfer.long");
	}

	/**
	 * Description: 转派任务
	 * @return void
	 * @author Binfeng.Yan
	 * @date 2023/3/4 17:10
	 */
	@Test
	public void test() {
		Instance instance = flowLongEngine.startInstanceByName("transfer", 1);
		System.out.println("instance=" + instance);
		List<Task> tasks = flowLongEngine.queryService().getActiveTasksByInstanceId(instance.getId());
		for(Task task : tasks) {
			flowLongEngine.taskService().createNewTask(task.getId(), 0, "test");
			flowLongEngine.taskService().complete(task.getId());
		}
	}


}

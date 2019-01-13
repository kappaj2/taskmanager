package com.ajk.taskman.service.impl;

import com.ajk.taskman.exception.TaskNotFoundException;
import com.ajk.taskman.model.Task;
import com.ajk.taskman.model.TaskStatus;
import com.ajk.taskman.pojo.TaskPojo;
import com.ajk.taskman.repository.TasksRepository;
import com.ajk.taskman.repository.UsersRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TaskServiceImplTest {

    private Fixture fixture;

    @Autowired
    private TaskServiceImpl taskService;

    @MockBean
    private TasksRepository tasksRepository;

    @MockBean
    private UsersRepository usersRepository;

    @Before
    public void setup() {
        fixture = new Fixture();
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void testGetTask() {

        Task task = new Task();
        task.setId(1);
        task.setTask("Test Task");
        task.setDescription("Test Description");
        task.setDateTime(new Date());
        task.setTaskStatusBean(new TaskStatus());

        when(tasksRepository.findByUserAndTaskId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(task);

        fixture.givenTaskIdIsSet(Integer.valueOf(1));
        fixture.givenUserIdIsSet(Integer.valueOf(1));
        fixture.whenGetTaskForUserIsCalled();
        fixture.thenTaskIdShouldBe(Integer.valueOf(1));
    }

    /**
     * Test retrieve task with userId and taskId which are not valid.
     */
    @Test
    public void testGetTaskWithInvalidReference() {

        Task task = new Task();
        task.setId(1);
        task.setTask("Test Task");
        task.setDescription("Test Description");
        task.setDateTime(new Date());
        task.setTaskStatusBean(new TaskStatus());

        when(tasksRepository.findByUserAndTaskId(Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new TaskNotFoundException("Cannot find task for userId and taskId"));

        fixture.givenTaskIdIsSet(Integer.valueOf(1));
        fixture.givenUserIdIsSet(Integer.valueOf(1));
        exception.expect(TaskNotFoundException.class);
        fixture.whenGetTaskForUserIsCalled();

    }

    public class Fixture {
        private Integer taskId;
        private Integer userId;

        private TaskPojo taskPojo;

        public void givenTaskIdIsSet(Integer taskId) {
            this.taskId = taskId;
        }

        public void givenUserIdIsSet(Integer userId) {
            this.userId = userId;
        }

        public void whenGetTaskForUserIsCalled() {
            taskPojo = taskService.getTask(userId, taskId);
        }

        public void thenTaskIdShouldBe(Integer taskId) {
            assertTrue("Expected taskId is : " + taskId, taskId == taskPojo.getId());
        }
    }
}
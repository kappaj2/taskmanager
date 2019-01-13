package com.ajk.taskman.service.impl;

import com.ajk.taskman.exception.UserNotFoundException;
import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.UserPojo;
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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserServiceImplTest {

    private Fixture fixture;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UsersRepository usersRepository;

    @Before
    public void setup() {
        fixture = new Fixture();
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Test the service returns the Pojo correctly. The repository is mocked, so it is basically the internal handling of the model that is tested.
     */
    @Test
    public void testFindUserWithValidId() {

        User user = new User();
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setUsername("TestUserName");
        user.setId(1);

        when(usersRepository.getOne(Mockito.anyInt()))
                .thenReturn(user);

        fixture.givenUserIdIsSet(Integer.valueOf(1));
        fixture.whenRetrieveUserIsCall();
        fixture.thenUserPojoIdShouldBe(Integer.valueOf(1));
    }

    /**
     * Test finding a user with invalid Id. Should throw UserNotFoundException.
     */
    @Test
    public void testFindUserWithInValidId() {

        when(usersRepository.getOne(Mockito.eq(2)))
                .thenThrow(new UserNotFoundException("User not found for id"));

        fixture.givenUserIdIsSet(Integer.valueOf(2));
        exception.expect(UserNotFoundException.class);
        fixture.whenRetrieveUserIsCall();
    }

    public class Fixture {

        private UserPojo userPojo;
        private Integer userId;

        public void givenUserIdIsSet(Integer userId) {
            this.userId = userId;
        }

        public void whenRetrieveUserIsCall() {
            userPojo = userService.findUser(userId);
        }

        public void thenUserPojoIdShouldBe(Integer userId) {
            assertTrue("Expected userId is : " + userId, userId == userPojo.getId());
        }

    }
}
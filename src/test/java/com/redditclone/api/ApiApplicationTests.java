package com.redditclone.api;

import com.google.common.net.HttpHeaders;
import com.redditclone.api.model.User;
import com.redditclone.api.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private UserRepository userRepository;

    private String getRootUrl() {
        return "http://localhost:" + port + "/user";
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testLogin() {
        User user = restTemplate.getForObject(getRootUrl() + "/login", User.class);
        System.out.println(user.getUsername());
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/list",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetUserById() {
        User user = restTemplate.getForObject(getRootUrl() + "/user/1", User.class);
        System.out.println(user.getUsername());
        Assert.assertNotNull(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("johndoe@testemail.com");

        ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/register", user, User.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateUser() {
        int id = 1;
        User user = restTemplate.getForObject(getRootUrl() + "/" + id, User.class);
        user.setUsername("Tesla");

        restTemplate.put(getRootUrl() + "/" + id, user);

        User updatedCar = restTemplate.getForObject(getRootUrl() + "/" + id, User.class);
        Assert.assertNotNull(updatedCar);
    }

    @Test
    public void testDeleteUser() {
        int id = 2;
        User user = restTemplate.getForObject(getRootUrl() + "/" + id, User.class);
        Assert.assertNotNull(user);

        restTemplate.delete(getRootUrl() + "/delete/" + id);

        try {
            user = restTemplate.getForObject(getRootUrl() + "/delete/" + id, User.class);
        } catch (final HttpClientErrorException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}

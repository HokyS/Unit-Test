package com.example.UnitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class MyUserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	MyUserController myUserController;

	@Autowired
	MyUserRepository myUserRepository;

	@Test
	void contextLoads() {
		Assertions.assertThat(myUserController).isNotNull();
	}

	@Test
	void createUserTest() throws Exception {
		MyUser savedUser = createPostUserRequest();
		MyUser getUser = getUserByIdRequest(savedUser.getId());

		assertThat(savedUser.equals(getUser));
	}

	@Test
	void getAllUsersTest() throws Exception {
		createPostUserRequest();
		createPostUserRequest();
		createPostUserRequest();
		MvcResult result = this.mockMvc.perform(get("/users/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		List<MyUser> usersFromResponse = objectMapper.readValue(result.getResponse().getContentAsByteArray(), List.class);

		assertThat(usersFromResponse.size()).isEqualTo(3);
	}

	@Test
	void updateUserTest() throws Exception {
		MyUser user = createPostUserRequest();
		String newName = "Giovanna";
		user.setName(newName);
		String studentJSON = objectMapper.writeValueAsString(user);
		mockMvc.perform(put("/users/" + user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Optional<MyUser> studentFromDB = myUserRepository.findById(user.getId());

		assertThat(studentFromDB.isPresent());
		assertThat(studentFromDB.get().equals(user));
	}

	@Test
	void deleteUserTest() throws Exception {
		MyUser user = createPostUserRequest();
		mockMvc.perform(delete("/user/" + user.getId()))
				.andDo(print())
				.andReturn();
		Optional<MyUser> studentFromDB = myUserRepository.findById(user.getId());

		assertThat(studentFromDB.isEmpty());
	}

	private MyUser getUserByIdRequest(Long id) throws Exception {
		MvcResult result = mockMvc.perform(get("/users/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String studentJSON = result.getResponse().getContentAsString();
		if (studentJSON.isEmpty()) return null;
		return objectMapper.readValue(studentJSON, MyUser.class);
	}

	private MyUser createUser() throws Exception {
		List<String> names = List.of("Giovanna", "Mirtha", "Lorena", "Giulia");
		List<String> surnames = List.of("Gialli", "Micelli", "Lucca", "Gironi");
		MyUser user = new MyUser();
		Random random = new Random();
		user.setName(names.get(random.nextInt(4)));
		user.setSurname(surnames.get(random.nextInt(4)));
		return user;
	}

	private MyUser createPostUserRequest() throws Exception {
		MyUser user = createUser();
		String studentJSON = objectMapper.writeValueAsString(user);
		MvcResult result = mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		studentJSON = result.getResponse().getContentAsString();
		return objectMapper.readValue(studentJSON, MyUser.class);
	}
}

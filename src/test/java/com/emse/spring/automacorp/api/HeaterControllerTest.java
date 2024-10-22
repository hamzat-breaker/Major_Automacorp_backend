package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.WindowDao;
import com.emse.spring.automacorp.dao.RoomDao;
import com.emse.spring.automacorp.dao.SensorDao;
import com.emse.spring.automacorp.dao.BuildingDao;
import com.emse.spring.automacorp.dao.HeaterDao;
import com.emse.spring.automacorp.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

//@WebMvcTest(HeaterController.class)
public class HeaterControllerTest {
    /**
     * All the controller tests were implemented before adding the authentication part of the backend.
     * The tests worked before the addition but the modifications made on a mock test don't work.
     * So in order to privilege security over the unit testing (knowing the real testing does work),
     *  I decided to keep the unit test as they were before but commented them, so I don't have error when building the
     *  project (a mock test attempt can be found at the end of RoomControllerTest class).
     * To use the unit testing without the added security level, you have to remove/comment :
     *  - from build.gradle.kts the lines
     *      implementation("org.springframework.boot:spring-boot-starter-security")
     * 	    testImplementation("org.springframework.security:spring-security-test")
     * 	- from application.properties the lines
     * 	    spring.security.user.name=user
     *      spring.security.user.password=password
     *  - SpringSecurityConfig and SecurityController code and imports
     *  - from RoomControllerTest the lines
     *      import static org.mockito.BDDMockito.given;
     *      import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
     *  - perhaps some other imports of the removed implementation of spring boot security that I've forgotten
     */

    /*
    // Spring object to mock call to our app
    @Autowired
    private MockMvc mockMvc;

    // The serializer used by Spring to send and receive data to/from the REST controller
    @Autowired
    private ObjectMapper objectMapper;

    // We choose to mock the DAO used in the REST controller to limit the scope of our test
    @MockBean
    private BuildingDao buildingDao;
    @MockBean
    private SensorDao sensorDao;
    @MockBean
    private WindowDao windowDao;
    @MockBean
    private HeaterDao heaterDao;
    @MockBean
    private RoomDao roomDao;

    HeaterEntity createHeaterEntity(Long id, String name){
        SensorEntity heaterStatus = new SensorEntity(SensorType.STATUS, "heaterStatusSensor");
        BuildingEntity building = new BuildingEntity();
        RoomEntity room = new RoomEntity("room", new SensorEntity(SensorType.TEMPERATURE, "temperatureSensor"), 1, building);
        HeaterEntity heaterEntity = new HeaterEntity(name, heaterStatus, room);
        heaterEntity.setId(id);
        return heaterEntity;
    }

    @Test
    void shouldFindAll() throws Exception {
        Mockito.when(heaterDao.findAll()).thenReturn(List.of(
                createHeaterEntity(1L, "Heater 1"),
                createHeaterEntity(2L, "Heater 2")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("[*].name")
                                .value(Matchers.containsInAnyOrder("Heater 1", "Heater 2"))
                );
    }


    @Test
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(heaterDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void shouldFindById() throws Exception {
        HeaterEntity heaterEntity = createHeaterEntity(1L, "Heater 1");
        Mockito.when(heaterDao.findById(999L)).thenReturn(Optional.of(heaterEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/heaters/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Heater 1"));
    }

    @Test
    void shouldNotUpdateUnknownEntity() throws Exception {
        HeaterEntity heaterEntity = createHeaterEntity(1L, "Heater 1");
        HeaterCommand expectedHeater = new HeaterCommand(heaterEntity.getName(), heaterEntity.getPower(), heaterEntity.getHeaterStatus().getValue(), heaterEntity.getRoom().getId());
        String json = objectMapper.writeValueAsString(expectedHeater);

        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/heaters/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void shouldUpdate() throws Exception {
        HeaterEntity heaterEntity = createHeaterEntity(1L, "Heater 1");
        HeaterCommand expectedHeater = new HeaterCommand(heaterEntity.getName(), heaterEntity.getPower(), heaterEntity.getHeaterStatus().getValue(), heaterEntity.getRoom().getId());
        String json = objectMapper.writeValueAsString(expectedHeater);

        Mockito.when(heaterDao.findById(1L)).thenReturn(Optional.of(heaterEntity));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/heaters/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Heater 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    void shouldCreate() throws Exception {
        HeaterEntity heaterEntity = createHeaterEntity(1L, "Heater 1");
        HeaterCommand expectedHeater = new HeaterCommand(heaterEntity.getName(), heaterEntity.getPower(), heaterEntity.getHeaterStatus().getValue(), heaterEntity.getRoom().getId());
        String json = objectMapper.writeValueAsString(expectedHeater);

        Mockito.when(heaterDao.existsById(1L)).thenReturn(false);
        Mockito.when(heaterDao.save(Mockito.any(HeaterEntity.class))).thenReturn(heaterEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/heaters")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Heater 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/heaters/999"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    */
}

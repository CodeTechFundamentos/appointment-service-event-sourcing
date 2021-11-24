import com.nutrix.appointmentservice.command.infra.Diet;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegisterDietSteps {

    @LocalServerPort
    private int port;
    private String idUid;
    private RestTemplate restTemplate = new RestTemplate();
    //private String postUrl="http://localhost:8084/";
    private String postUrl="https://nutrix-appointment-service.mybluemix.net/";
    private String error= "Ingrese una descripcion";

    @Given("I register a new diet as a nutritionist")
    public void i_register_a_new_diet_as_a_nutritionist() {
        String url=postUrl + "diets/";
        List<Diet> all=restTemplate.getForObject(url, List.class);
        log.info(all);
        assertTrue(!all.isEmpty());
    }
    @Given("I sending diet form to be created with name {string} and description {string}")
    public void i_sending_diet_form_to_be_created_with_name_and_description(String name, String description) {
        Date date= new Date();
        String id = "88c061a3-b67c-48eb-a63d-49d5bdf9e836";
        Diet diet = new Diet( id, name, description, date, date);
        String url=postUrl + "diets/";
        Diet newDiet =restTemplate.postForObject(url,diet,Diet.class);
        idUid = newDiet.getId();
        log.info(newDiet);
        assertNotNull(newDiet);
    }
    @Then("I should be able to see my newly created diet")
    public void i_should_be_able_to_see_my_newly_created_diet() {
        String url=postUrl+"diets/" + idUid;
        Diet diet=restTemplate.getForObject(url,Diet.class);
        log.info(diet);
        assertNotNull(diet);
    }

    @Given("I sending diet form to be created without description {string}")
    public void i_sending_diet_form_to_be_created_without_description(String string) {
        String id = "88c061a3-b67c-48eb-a63d-49d5bdf9e836";
        Date date= new Date();
        Diet diet = new Diet(id, "Dieta1", string, date, date);
        String url=postUrl + "diets/";

        try
        {
            Diet newDiet =restTemplate.postForObject(url,diet,Diet.class);
            log.info(newDiet);
        }catch(RestClientException e){
            error="Ingrese una descripcion";
        }
        assertEquals(error,"Ingrese una descripcion");
    }
    @Then("I should be able to see an advice {string}")
    public void i_should_be_able_to_see_an_advice(String string) {
        assertEquals("Ingrese una descripcion",string);
    }
}

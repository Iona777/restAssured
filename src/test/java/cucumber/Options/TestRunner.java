package cucumber.Options;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions"},
        strict = true //To suppress warning 'WARNING: By default Cucumber is running in --non-strict mode.'
)
public class TestRunner
{

}

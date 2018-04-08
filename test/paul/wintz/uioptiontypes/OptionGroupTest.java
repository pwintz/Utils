package paul.wintz.uioptiontypes;

import static org.hamcrest.Matchers.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class OptionGroupTest {

    OptionGroup optionGroup = new OptionGroup("A Description");

    @Mock
    OptionGroup.Observer changeListener;
    
    UserInputOption<?> dummyOption0 = new UserInputOption<String>("dummy0") {};
    UserInputOption<?> dummyOption1 = new UserInputOption<String>("dummy1") {};
    
    // CONSTRUCTORS
    
    @Test
    public void setsDescriptionToClassNameIfNoDescriptionGiven() {
        OptionGroup anOptionGroup = new OptionGroup();
        
        assertThat(anOptionGroup.getDescription(), is(equalTo("OptionGroup")));
    }
    
    @Test
    public void setsDescriptionToArgument() {
        OptionGroup anOptionGroup = new OptionGroup("A Description");
        
        assertThat(anOptionGroup.getDescription(), is(equalTo("A Description")));
    }

    @Test
    public void containsNoItemsIfNonePassedToConstructor() {
        OptionGroup anOptionGroup = new OptionGroup();
        
        assertThat(anOptionGroup.size(), is(equalTo(0)));
    }
    
    @Test
    public void containsItemsPassedToConstructor() {
        OptionGroup anOptionGroup = new OptionGroup(dummyOption0);
        
        assertThat(anOptionGroup.get(0), is(equalTo(dummyOption0)));
    }
    
    // TO STRING
    
    @Test
    public void toStringDoesNotCrash() {
        //noinspection ResultOfMethodCallIgnored
        optionGroup.toString();
    }
    
        
}

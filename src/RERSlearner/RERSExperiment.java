package RERSlearner;

import com.google.common.collect.ImmutableSet;
import de.learnlib.api.SUL;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by rick on 17/03/2017.
 */
public class RERSExperiment {
    /**
     * Example of how to learn a Mealy machine model from one of the compliled RERS programs.
     * @param args
     * @throws IOException
     */
    public static void main(String [] args) throws IOException {
        // Load the System Under Learning (SUL)
        SUL<String,String> sul = new ProcessSUL("/Users/sicco/Dropbox/STR_20162017/ReachabilityRERS2017/Problem10/a.out");


        // the input alphabet
        Collection<String> inputAlphabet = ImmutableSet.of("1","2","3","4","5");

        try {
            // runControlledExperiment for detailed statistics, runSimpleExperiment for just the result
            //BasicLearner.runControlledExperiment(sul, BasicLearner.LearningMethod.TTT, BasicLearner.TestingMethod.RandomWalk, inputAlphabet);
            BasicLearner.runControlledExperiment(sul, BasicLearner.LearningMethod.LStar, BasicLearner.TestingMethod.WMethod, inputAlphabet);
        } finally {
            if (sul instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) sul).close();
                } catch (Exception exception) {
                    // should not happen
                }
            }
        }
    }
}
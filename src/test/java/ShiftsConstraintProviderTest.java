import org.example.HalfHourTimeslot;
import org.example.ShiftsConstraintProvider;
import org.example.ShiftsSolution;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Objects;

public class ShiftsConstraintProviderTest {

    private final ConstraintVerifier<ShiftsConstraintProvider, ShiftsSolution> constraintVerifier = ConstraintVerifier.build(
            new ShiftsConstraintProvider(), ShiftsSolution.class, HalfHourTimeslot.class);


    private ShiftsSolution deserialize(URL pathToSerializedObject)
    {
        ShiftsSolution solution = null;
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(pathToSerializedObject.getPath());
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            solution = (ShiftsSolution)in.readObject();

            in.close();
            file.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

        return solution;
    }

    @Test
    public void expectedScore() {
        ShiftsSolution solution = deserialize(Objects.requireNonNull(getClass().getClassLoader().getResource("testSolution.ser")));

        constraintVerifier.verifyThat()
                .given(solution)
                .scores(HardMediumSoftScore.of(0, -500011,0));
    }
}

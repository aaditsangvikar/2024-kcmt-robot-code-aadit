package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IntakeCommand extends Command {
    private final IntakeSubsystem intakeSubsystem;

    private boolean continuous;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * Command to intake a note
     * @param subsystem The instance of {@link IntakeSubsystem}
     */
    public IntakeCommand(IntakeSubsystem subsystem, boolean continuous) {
        this.intakeSubsystem = subsystem;
        this.continuous = continuous;

        addRequirements(subsystem);
    }

    public void initialize() {
        if (!continuous) {
            // Asynchronously run the intake motor, checking the linebreak every 5 milliseconds
            executor.scheduleAtFixedRate(() -> {
                if (!this.intakeSubsystem.getFrontLinebreak()) {
                    intakeSubsystem.setSpeed(0.6);
                } else {
                    intakeSubsystem.setSpeed(0);
                }
            }, 0, 5, TimeUnit.MILLISECONDS);
        } else {
            // Asynchronously run the intake motor every 20 milliseconds
            executor.scheduleAtFixedRate(() -> {
                intakeSubsystem.setSpeed(0.6);
            }, 0, 20, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void end(boolean interrupted) {
        executor.shutdown();
        intakeSubsystem.setSpeed(0);
    }
}

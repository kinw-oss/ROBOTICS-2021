package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="TheOneAndOnly", group="Linear Opmode")

public class TheOneAndOnly extends LinearOpMode {


    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor firstLauncher;
    private DcMotor secondLauncher;
    private Servo lifter;
    private Servo shifter;

    private double shooterPower = 0.5;

    @Override
    public void runOpMode() {

        waitForStart();

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        firstLauncher = hardwareMap.get(DcMotor.class, "firstLauncher");
        secondLauncher = hardwareMap.get(DcMotor.class, "secondLauncher");

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        firstLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        lifter = hardwareMap.get(Servo.class, "lifter");
        shifter = hardwareMap.get(Servo.class, "shifter");

        shifter.setPosition(1);

        while (opModeIsActive()) {

            //shitty way of assigning power
            double rightPower = -gamepad1.right_stick_y;
            double leftPower = gamepad1.left_stick_y;

            backRight.setPower(rightPower);
            frontRight.setPower(rightPower);
            backLeft.setPower(leftPower);
            frontLeft.setPower(leftPower);

            //charges shooter
            if (gamepad1.left_trigger > 0) {
                setShooterPower(shooterPower);
            } else if (gamepad1.left_trigger == 0) {
                setShooterPower(0);
            }

            //shoots one ring...shooter must be charged first with left trigger
            if (gamepad1.a) {
                shifter.setPosition(0.57);
                sleep(500);
                shifter.setPosition(1);
            }

            //increments power of shooter up
            if (gamepad1.dpad_up) {
                shooterPower += 0.01;
                sleep(200);
            }
                                                                            
            //increments power of shooter down
            if (gamepad1.dpad_down) {
                shooterPower -= 0.01;
                sleep(200);
            }
            //moves lifter down incrementally, so it doesn't jerk down
            if (gamepad1.x) {
                double position = 0.87;

                lifter.setPosition(position);

                for (int i=0; i<10; i++) {
                    position -= 0.02;
                    lifter.setPosition(position);
                }
            }

            //moves lifter up
            if (gamepad1.y) {
                lifter.setPosition(0.87);
            }

            telemetry.addData("shooterPower", shooterPower);
            telemetry.update();
        }
    }

    //stupid, unnessecary method. 
    private void setShooterPower(double power) {
        firstLauncher.setPower(power);
        secondLauncher.setPower(power);
    }

}

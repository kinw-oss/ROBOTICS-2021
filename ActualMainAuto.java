package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCurrentGame;

@Autonomous(name = "ActualMainAuto")
public class ActualMainAuto extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "ASDZYK3/////AAABmcPs4lemSEC6kHBUD5lpAQRxx5U+vJZBQeR+erp7dMnyf5eZnu5FO9XXrTfzX2BSltfkw3Rn2lpjoxhIk12n7fFiRQoi0CLQFbulEDE4FXJxJZWD5jHsDsn3J/Dp4flFNSmUiGDe88clC0BEplIXOEdqPqr1JV4WN2fR/9jVDFt2BUW/l+hI42++7hmaEgmPb69uIvOVpIVLvTqUJ4i78PxlGHW+uYj6tZjEYJbJBQrJC/YBJ2K2MP+iA+UVZhfirrDXct87srSKAim4p0EHNMvdUczqh29L/+5KZUHtGGYuNvZAvp/rbceDlgz8EIoxHgxTMWuAfGJeD0lfv+nJ5zooRNqLYeEO6SnIr6oAvsHV";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor firstLauncher;
    private DcMotor secondLauncher;
    private DcMotor wobbler;
    private Servo lifter;
    private Servo shifter;
    private Servo grabber;


    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }     //Inits Vuforis

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.activate();
    } //Inits Tfod

    private void setShooterPower(double power) {
        firstLauncher.setPower(power);
        secondLauncher.setPower(power);
        sleep(1000);
    }

    private void runToPosition() {
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    } //Sets motor encoders to RUN_TO_POSITION

    private void setMotorPosition(int rightPosition, int leftPosition) {
        backRight.setTargetPosition(rightPosition);
        frontRight.setTargetPosition(rightPosition);
        backLeft.setTargetPosition(leftPosition);
        frontLeft.setTargetPosition(leftPosition);
    }

    private void initMotorsServos() {

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        firstLauncher = hardwareMap.get(DcMotor.class, "firstLauncher");
        secondLauncher = hardwareMap.get(DcMotor.class, "secondLauncher");
        wobbler = hardwareMap.get(DcMotor.class, "wobbler");

        lifter = hardwareMap.get(Servo.class, "lifter");
        shifter = hardwareMap.get(Servo.class, "shifter");
        grabber = hardwareMap.get(Servo.class, "grabber");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);

        firstLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbler.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbler.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        grabber.setPosition(0.55);
        sleep(200);
        wobbler.setPower(0.7);
        sleep(700);
        wobbler.setPower(0);
        lifter.setPosition(0.87);
        shifter.setPosition(1);


    }           //It inits motors. And servos. Obviously.

    private String getTargetZone() {

        String targetZone;

        // Get a list of recognitions from TFOD.
        List<Recognition> recognitions = tfod.getUpdatedRecognitions();

        if (recognitions.size() == 0) {
            telemetry.addData("Target Zone:", "A");
            targetZone = "A";
        } else {
            if (recognitions.get(0).getLabel().equals("Quad")) {
                telemetry.addData("Target Zone:", "C");
                targetZone = "C";
            } else {
                telemetry.addData("Target Zone:", "B");
                targetZone = "B";
            }
        }
        telemetry.update();
        return targetZone;
    }      //Returns A, B, or C depending on amount of rings

    private void movement(int rightDistance, int leftDistance, double power) {

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setMotorPosition(rightDistance, leftDistance);

        backRight.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        frontLeft.setPower(power);

        runToPosition();

        while ((frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy()) && opModeIsActive()) {
            //Waits for the motors to finish moving & prints 2 CurrentPositions
            telemetry.addData("FrontLeft Position: ", frontLeft.getCurrentPosition() + "  busy=" + frontLeft.isBusy());
            telemetry.addData("FrontRight Position: ", frontRight.getCurrentPosition() + "  busy=" + frontRight.isBusy());
            telemetry.update();
        }

        backRight.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        frontLeft.setPower(0);

        sleep(250);

    } //Pretty Obvious


    private void shoot(double power) {
        setShooterPower(power);
        sleep(500);
        telemetry.addData("power", firstLauncher.getPower());
        telemetry.update();
        for (int i = 0; i < 3; i++) {
            shifter.setPosition(0.57);
            sleep(500);
            shifter.setPosition(1);
            sleep(800);
        }
        setShooterPower(0);
    }

    @Override
    public void runOpMode() {

        telemetry.addData("Ready", "False");
        telemetry.update();
        initVuforia();
        initTfod();
        initMotorsServos();

        telemetry.addData("Ready", "True");
        telemetry.update();

        waitForStart();
        if (opModeIsActive()) {

            movement(5000, 5000, 1);


            sleep(5000);
            String targetZone = getTargetZone();

            telemetry.addData("Target Zone:", targetZone);
            telemetry.update();

            tfod.deactivate();

            movement(8000, 8000, 1);

            movement(0, 800, 1);

            shoot(0.46);

            if (targetZone.equals("A")) {

                movement(-2000, -2000, 1);

                movement(13000, 0, 1.0);

                telemetry.addData("cock", "balls");
                telemetry.update();

                sleep(2000);

                //WHAT THE FUUUUUUUUCK

                grabber.setPosition(0.9);
                sleep(500);
                wobbler.setPower(-0.7);
                sleep(800);
                wobbler.setPower(0);
                backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                backRight.setDirection(DcMotorSimple.Direction.REVERSE);
                frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
                movement(3000, 3000, 1.0);
                sleep(500);

            } else if (targetZone.equals("B")) {

                movement(14000, 6000, 1.0);
                lifter.setPosition(0);
                grabber.setPosition(0.9);
                sleep(500);
                wobbler.setPower(-0.7);
                sleep(800);
                wobbler.setPower(0);

            } else {
                movement(800, 0, 1);
                backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);

                movement(2000, 2000, 1);
                backRight.setDirection(DcMotorSimple.Direction.REVERSE);
                frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

                movement(10000, 10000, 1);

                lifter.setPosition(0);
                grabber.setPosition(0.9);
                sleep(500);
                wobbler.setPower(-0.7);
                sleep(800);
                wobbler.setPower(0);

            }


        }

    }

}

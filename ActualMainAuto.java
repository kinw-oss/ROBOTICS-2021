package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "ActualMainAuto")
public class ActualMainAuto extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "ASDZYK3/////AAABmcPs4lemSEC6kHBUD5lpAQRxx5U+vJZBQeR+erp7dMnyf5eZnu5FO9XXrTfzX2BSltfkw3Rn2lpjoxhIk12n7fFiRQoi0CLQFbulEDE4FXJxJZWD5jHsDsn3J/Dp4flFNSmUiGDe88clC0BEplIXOEdqPqr1JV4WN2fR/9jVDFt2BUW/l+hI42++7hmaEgmPb69uIvOVpIVLvTqUJ4i78PxlGHW+uYj6tZjEYJbJBQrJC/YBJ2K2MP+iA+UVZhfirrDXct87srSKAim4p0EHNMvdUczqh29L/+5KZUHtGGYuNvZAvp/rbceDlgz8EIoxHgxTMWuAfGJeD0lfv+nJ5zooRNqLYeEO6SnIr6oAvsHV";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private DcMotor backRight, frontRight, frontLeft, backLeft, firstLauncher, secondLauncher, wobbler;
    private Servo lifter, shifter, grabber;
    private ElapsedTime timer = new ElapsedTime();

    protected void initHardware() {
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

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        firstLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbler.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbler.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        grabber.setPosition(0.2);
        lifter.setPosition(0.6);

    }

    protected void initVision() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.activate();
    }

    protected void movement(int FLDistance, int FRDistance, int BLDistance, int BRDistance, double power) {

        timer.reset();

        backRight.setTargetPosition(backRight.getCurrentPosition()   + BRDistance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + FRDistance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition()     + BLDistance);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition()   + FLDistance);

        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        frontLeft.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy()) && opModeIsActive() && timer.time() < 4) {
            //Waits for the motors to finish moving & prints 2 CurrentPositions
            telemetry.addData("frontLeft=", frontLeft.getCurrentPosition());
            telemetry.addData("frontRight=", frontRight.getCurrentPosition());
            telemetry.addData("backLeft=", backLeft.getCurrentPosition());
            telemetry.addData("backRight=", backRight.getCurrentPosition());
            telemetry.addData("time: ", timer.time());
            telemetry.update();

        }

        backRight.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        frontLeft.setPower(0);

    }

    protected String getTargetZone() {

        String targetZone = null;

        // Get a list of recognitions from TFOD.
        List<Recognition> recognitions = tfod.getRecognitions();

        if (recognitions.size() == 0) {

            targetZone = "A";
        }
        if (recognitions.size() > 0 && recognitions.get(0).getLabel().equals("Quad")) {

            targetZone = "C";
        } else if (recognitions.size() > 0 && recognitions.get(0).getLabel().equals("Single")){

            targetZone = "B";
        }


        return targetZone;
    }

    protected void shoot3(double power) {
        firstLauncher.setPower(power);
        secondLauncher.setPower(power);
        sleep(1500);
        for (int i = 0; i < 3; i++) {
            shifter.setPosition(0.57);
            sleep(400);
            shifter.setPosition(1);
            sleep(800);
        }
        firstLauncher.setPower(0);
        secondLauncher.setPower(0);

    }

    @Override
    public void runOpMode() {

        telemetry.addData("Ready", "False");
        telemetry.update();
        
        initHardware();
        initVision();

        telemetry.addData("Ready", "True");
        telemetry.update();

        waitForStart();

        if (opModeIsActive() && !isStopRequested()) {

            //move up to read
            movement(700, 700, 700, 700, 0.3);

            sleep(2000);
            String targetZone = getTargetZone();
            tfod.shutdown();
            telemetry.addData("Target Zone:", targetZone);
            telemetry.update();

            //move up to shoot, then shoot
            movement(1500, 1500, 1500, 1500, 0.3);
            movement(150, 0, 150, 0, 0.2);

            grabber.setPosition(0.2);
            wobbler.setPower(0.8);
            sleep(700);
            wobbler.setPower(0);
            lifter.setPosition(0.95);

            shoot3(0.48);

            //begin placement of wobble goal

            switch (targetZone) {
                case "A":

                    movement(700, 1000, 700, 1000, 0.3);

                    grabber.setPosition(0.9);
                    sleep(100);
                    wobbler.setPower(-0.8);
                    sleep(700);
                    wobbler.setPower(0);

                    break;
                case "B":

                    movement(1100, 1100, 1100, 1100, 0.5);
                    sleep(100);
                    movement(800, 0, 800, 0, 0.3);

                    grabber.setPosition(0.9);
                    sleep(100);
                    wobbler.setPower(-0.8);
                    sleep(700);
                    wobbler.setPower(0);

                    movement(-700, -700, -700, -700, 0.6);

                    break;
                case "C":
                    movement(-150, 0, -150, 0, 0.2);
                    movement(2800, 2800, 2800, 2800, 0.7);

                    grabber.setPosition(0.9);
                    sleep(100);
                    wobbler.setPower(-0.9);
                    sleep(700);
                    wobbler.setPower(0);

                    movement(-1700, -1700, -1700, -1700, 0.7);
                    break;
            }

        }

    }

}



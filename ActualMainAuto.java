package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backRight.setTargetPosition(BRDistance);
        frontRight.setTargetPosition(FRDistance);
        backLeft.setTargetPosition(BLDistance);
        frontLeft.setTargetPosition(FLDistance);

        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        frontLeft.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy()) && opModeIsActive()) {
            //Waits for the motors to finish moving & prints 2 CurrentPositions
            telemetry.addData("frontLeft=", frontLeft.getCurrentPosition());
            telemetry.addData("frontRight=", frontRight.getCurrentPosition());
            telemetry.addData("backLeft=", backLeft.getCurrentPosition());
            telemetry.addData("backRight=", backRight.getCurrentPosition());
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
            sleep(500);
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

            movement(700, 700, 700, 700, 0.3);


            sleep(3000);
            String targetZone = getTargetZone();

            tfod.shutdown();

            telemetry.addData("Target Zone:", targetZone);
            telemetry.update();


            movement(2300, 0, 0, 2300, 0.5);

            movement(700, 700, 700, 700, 0.5);

            wobbler.setPower(0.8);
            sleep(700);
            wobbler.setPower(0);

            lifter.setPosition(0.86);

            firstLauncher.setPower(0.41);
            secondLauncher.setPower(0.41);
            sleep(1500);
            for (int i = 0; i < 3; i++) {
                shifter.setPosition(0.57);
                sleep(500);
                shifter.setPosition(1);
                sleep(500);
            }
            /*
            switch (targetZone) {
                case "A":

                    //movement(-2000, -2000, 1);

                    //movement(13000, 0, 1.0);

                    telemetry.addData("cock", "balls");
                    telemetry.update();

                    //WHAT THE FUUUUUUUUCK

                    grabber.setPosition(0.9);
                    sleep(500);
                    wobbler.setPower(-0.7);
                    sleep(800);
                    wobbler.setPower(0);
                    telemetry.addData("cock&ball", "torture");
                    telemetry.update();
                    sleep(500);
                    //movement(-3000, -3000, 1.0);

                    telemetry.addData("William", "Perri");
                    telemetry.update();
                    break;
                case "B":

                    //movement(14000, 6000, 1.0);
                    telemetry.addData("cock&ball", "torture");
                    telemetry.update();
                    lifter.setPosition(0);
                    grabber.setPosition(0.9);
                    sleep(500);
                    wobbler.setPower(-0.7);
                    sleep(800);
                    wobbler.setPower(0);
                    telemetry.addData("William", "Perri");
                    telemetry.update();
                    sleep(3000);

                    break;
                case "C":
                    //movement(800, 0, 1);

                    //movement(10000, -10000, 1);

                    //movement(-20000, -20000, 1);

                    telemetry.addData("cock&ball", "torture");
                    telemetry.update();
                    lifter.setPosition(0);
                    grabber.setPosition(0.9);
                    sleep(500);
                    wobbler.setPower(-0.7);
                    sleep(800);
                    wobbler.setPower(0);

                    telemetry.addData("William", "Perri");
                    telemetry.update();
                    //movement(20000, 20000, 1);
                    telemetry.addData("cock", "balls");
                    telemetry.update();

                    break;
            }
            */
        }

    }

}



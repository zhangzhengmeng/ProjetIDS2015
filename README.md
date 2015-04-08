# ProjetIDS2015
Polytech Grenoble RICM4
http://air.imag.fr/index.php/Proj-2014-2015-Interactive_Digitale_Signage

Setup: target: windows 64 bits
       1.for NFC
       - install WampServer
       - copy the "PHP-VOXYGEN" folder in the "www" folder of WampServer.
       2.for Kinect and processing
       - install nite-win64-1.5.2.21-dev
       - install openni-win64-1.5.4.0-dev
       - disconnect from internet and connect Kinect(There is conflict of pilot between Kinect Sensor Kinect and Windows Kinect)
       - install SensorKinect092-Bin-Win64-v5.1.2.1
       - install processing 2.2.1
       - copy libs into processing lib folder

Run:
       - run WampServer
       - run the executable jar "IDS" in the "IDS EXE" folder
       - run processing
       - open "IDS.pde" in processing
       - the page should be at localhost:8000

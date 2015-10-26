# README #
###################################################################################################
### Repository containing source code and executable file for Open Source EM Solver application ###
###################################################################################################

* The open source EM solver is a GUI front-end for the text based NEC2 engine that aims to abstract the process of creating NEC input files from the user.
* Version : 0.11a
* [Source Code](https://bitbucket.org/age1208/open-em-solver)

### Application Setup ###

(1) Firstly ensure that you have the latest version of the Java Development Kit installed on your Linux system (8u65 at the time of writing) if not proceed with the following instructions:
	- Go to http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html or Google Java Development Kit and download the 64 bit (newer computer) or 32 bit (older)
	  version of the JDK.
	  
	- Next go to the Download folder that the JDK is located in (usually /home/username/Downloads for Debian systems), right click on the tar ball and extract it into the current Downloads folder
	
	- Move the extracted folder to /usr/lib/jvm using the following terminal command (be sure to open terminal in the current Downloads folder)
		-- sudo mv jdk1.8.0_65 /usr/lib/jvm/oracle_jdk8
		
	- Now type the following terminal command in the current Downloads folder
		-- sudo nano /etc/profile.d/oraclejdk.sh
		
	- And finally insert the following text into the file you have just created
		-- 	export J2SDKDIR=/usr/lib/jvm/oracle_jdk8
			export J2REDIR=/usr/lib/jvm/oracle_jdk8/jre
			export PATH=$PATH:/usr/lib/jvm/oracle_jdk8/bin:/usr/lib/jvm/oracle_jdk8/db/bin:/usr/lib/jvm/oracle_jdk8/jre/bin
			export JAVA_HOME=/usr/lib/jvm/oracle_jdk8
			export DERBY_HOME=/usr/lib/jvm/oracle_jdk8/db
	
	- Press (Ctrl+X) To exit the nano text editor and then type (y) to confirm that you wish to save the oraclejdk.sh file
	
	- Restart your Linux computer. Upon logging back in simply typing "java" in the terminal should bring up a bunch of java specific commands. The java compiler is now installed

(2) Navigate to the folder "Antenna Solver" that contains the executable openEMSolver.jar file in the terminal for example if the Antenna Solver folder were located on my desktop I would type the following command
	- cd /home/username/Desktop/Antenna\ Solver
	- Note the \ in Linux is used in the terminal to denote a space

(3) To run the application from the terminal simply type the following command in the Antenna Solver folder
	- java -jar openEMSolver.jar
	- This will run the application using the latest JDK Version 8
	- Alternatively in the file explorer you can right click on openEMSolver.jar and select "Open With" and then enter a custom command "java -jar openEMSolver.jar" and then double click on the icon.
	
### Tutorial: Using the Application ###

(1) To create a new Antenna Structure in space consisting of various antenna assemblies or primitive wire elements do the following:

	- Click on "File"
	- Click on "New Structure"
	- Give your structure a name that is not blank or contains a full stop and click "Submit"
	- Click on "Add Assemblies"
	- Click on the drop down box menu underneath the label "Select Assembly Type"
	- Click on the Assembly that you wish to add for example say we wish to add a simple Dipole.template select this option.
	- Notice how the GUI dynamically updates to fit the parameters of a Dipole antenna.
	- Change the text boxes below the Dipole.template box to suite the parameters you want your Dipole to have for example say TotalLength = 0.5 and set Frequency to 300MHz at the top of the menu.
	- Once you are satisfied with the Dipole parameters click on "Add Assembly"
	- Currently you have one of four options:
		(1) Complete Structure - If the complete structure button is pressed the NEC file is written using the Frequency specifications provided and antenna assemblies added.
		
		(2) Assembly Operations - If the assembly is selected in the list on the right of the window the following options are available:
			- Select (1_Dipole) in the list and observe the following buttons:
				-- If you wish to modify parameters pertaining to the selected assembly do so in the original text boxes and then select the "Edit Assembly" button.
				-- If you wish to remove an antenna assembly select it in the list and click the "Remove Assembly" button.
				-- Finally if you wish to rename an antenna assembly enter a new name in the text box provided that is not empty and does not have a fullstop and click "Rename Assembly".
				
	- Once the Complete Structure button has been pressed the "Visualize Structure" button becomes click-able. Pressing this button will execute xnecview and visualize the structure that has been created.
	- Thereafter to solve the antenna geometry press "Simulate". This will write a .out file.
	- To view the results of the simulation click the "Results" button.
	
(2) To open an existing Antenna Structure from an existing NEC file do the following:
	- Click "File"
	- Click "Open Existing Structure"
	- Navigate to your existing NEC file. This NEC file must be located in the same directory as the Antenna Solver application itself (this is a known issue).
	- Select the "Open" button
	- Click on "Visualize Structure", "Simulate" and "Results" to perform the same operations as stated above on the existing structure.
	
### Dependencies ###

* This program is currently dependent on two executable files namely xnecview and nec2c and cannot visualize and simulate respectively without these packages.

### Contribution guidelines ###

* Should you wish to add your own unit tests ensure that you are using JUnit 4 in a compatible Java IDE (Eclipse, Netbeans etc.)
* Source code is fully modifiable under GPL v2.0 license see the LICENSE file.
* As is in accordance with the license please make your code changes available to all.

### Known issues ###

* A known issue is currently that the existing NEC file that is selected must be located in the same folder as the executable file in order for the application to function.

### Developer Contacts ###

* If you have any questions do not hesitate to email adrianodegouveia@gmail.com
* If you wish to contact the co-developer email nathanjordaan@gmail.com

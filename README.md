# SC2002-Assignment

This is the GitHub repository for NTU SC2002 Assignment AY24/25 Semester 2. The application is a Build-To-Order (BTO) Management System designed for applicants and HDB staffs to view, apply and manage for BTO projects.

## Table of Contents

- [Folders in this project](#folders-in-this-project)
- [UML Diagrams](#uml-diagrams)
- [Compiling](#compiling-this-project)
- [Contributors](#contributors)

## Folders in this project
- src: The source code of the program
- bin: The folder containing compiled class files
- data: The data used in this program
- report: The assignment report, containing main report, UML diagrams: class diagram and sequence diagram, test outputs, and declaration of GAI use
- Testcase: The testcase folder
- html: Javadocs link

## UML Diagrams
To better view the UML Class Diagram, join this link and open by draw.io: https://drive.google.com/file/d/1hVMFniqV5udNdNzPkvzypWfH-UQJbTvX/view?usp=sharing

To better view the UML Sequence Diagram, join this link: https://cedar-fine-96a.notion.site/sequence-diagram-main-1dee971444e480249fa8c8977ed74fa9


## Compiling this project

### Requirements
- Java Development Kit (JDK) 21 or later

### Compilation
1. Open a terminal or command prompt
2. Navigate to the project root directory:
    ```
    cd SC2002-GRP5
    ```
3. Create the `bin` directory if it does not exist:
    ```
    mkdir bin
    ```
4. Compile the source code:
    ```
    & javac -encoding UTF-8 -d bin -sourcepath src src/main/Main.java
    ```
5. Run the program:
    ```
    java -cp bin main.Main
    ```
    
**Notes:**
- If you encounter old class errors, delete all files in the `bin` directory and recompile.
- Ensure your source files are saved with UTF-8 encoding to avoid the character issues.
- Samples are located in the `data` folder.

## Contributors
- Team members:
  - Nguyen Tran Thanh Lam
  - Nguyen Le Tam
  - Nguyen Tran Chien
  - Moorkkanattu Sunil Keerthana

# Linear Equation Solver

This program solves a system of linear equations with complex variables in case of single determined solution, in other cases it displays whether system has no solution or has infinite number of solutions.

This repository is implementation of one of the projects which is recommended by [HyperSkill platform](https://hyperskill.org) to learn Java development.   

* __Module 1__ - simple equation solver (ax + b = 0)
* __Module 2__ - equation solver of a system with two variables
* __Module 3__ - equation solver of a system of multiple variables (without special cases)
* __Module 4__ - equation solver of a system of multiple variables with all possible special cases (no solution, infinite solutions, single solution)
* __Module 5__ - equation solver of a system of multiple complex variables with all possible special cases (project dir: SolverApplication)

The final application is located in `SolverApplication` directory. 

## Getting started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You should have these programs installed on your local machine:

* Java 11 and higher
* IntelliJ IDEA Community (or Ultimate) Edition

### Installing

Getting source:
```
git clone https://github.com/niskhakov/LinearEquationSolver
cd LinearEquationSolver/SolverApplication/
// Open IntelliJ IDEA and open project located in this directory
```

Configuring IDEA:
```
Open `File`->`Project Structure` and choose appropriate SDK.
In this window you also should specify `Project compiler output` as `(Project_Dir)\out`
-> Press OK
Open context menu of `src` folder and click `Mark Directory as` -> `Sources Root`. 
Next click `Add Configuration...` button and press top-left `+`-> `Application`.
```

Application Tab:
```
Name: Release (or as you like)
Main class: solver.Main (you can configure it with `...` button)
VM options: (leave blank)
Program arguments: -in in.txt -out out.txt
...
-> Press OK
```

After all these steps you can run application in IntelliJ IDEA. 

### Usage

Run program with command: `java solver.Main -in in.txt -out out.txt` or `java solver.Main -h`
* -in in.txt - specifies path to the file with linear system in matrix form
* -out out.txt - specifies path to the output file where solution will be written
* -h - program help

## Authors
* **Nail Iskhakov** 
* **HyperSkill Platform** - https://hi.hyperskill.org/about
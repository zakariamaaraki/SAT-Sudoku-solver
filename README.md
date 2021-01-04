# SAT-Sudoku-solver

Implementation of n x n SAT Sudoku solver using minisat based on the paper by Inês Lynce and Jöel Ouaknine called Sudoku as a SAT Problem [https://people.mpi-sws.org/~joel/publications/sudoku05.pdf](https://people.mpi-sws.org/~joel/publications/sudoku05.pdf).

Sudoku is a number placement puzzle where one assigns a number to each cell.
Consider the 9 × 9 puzzle shown in this Figure. The aim is to assign a number
from 1 to 9 in each cell so that each row, column, and block contains only one
instance of each number. A Sudoku puzzle can be regarded as a SAT problem.
![Alt text](./sudoku.png.png?raw=true "Sudoku")

## Prerequisites

To run this project you need a docker engine running on your machine.

## Setup

Build docker image by typing the following command :

```
docker image build . -t sudoku
```

Run the container

```
docker container run -v /home:/home -it sudoku
```

## Author

- **Zakaria Maaraki** - _Initial work_ - [zakariamaaraki](https://github.com/zakariamaaraki)

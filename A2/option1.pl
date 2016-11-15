% Similar procedure to how it was done in A1:
%
% Test setup is like so:
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Generating random states... Done.
%% Robot is at pos(2, 3) facing North
%% ╔════════╗
%% ║░░░░░░██║
%% ║██░░░░░░║
%% ║░░░░¤¤↑↑║
%% ║¤¤¤¤░░░░║
%% ╚════════╝
%% Search using BFS... Done.
%% pos(row, col), Dir, action
%% pos(2, 3), North, start
%% pos(2, 3), West, left
%% pos(2, 2), West, move
%% pos(2, 2), West, suck
%% pos(2, 1), West, move
%% pos(2, 1), South, left
%% pos(3, 1), South, move
%% pos(3, 1), South, suck
%% pos(3, 1), West, right
%% pos(3, 0), West, move
%% pos(3, 0), West, suck
%% total cost: 290
%% Depth: 11
%% Time : 68 ms
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% But as 'rules' in prolog:
% Randomly choosen robot start position. ex:
startPos(1, 0).
% Random orientation. ex:
startOri(South).
% Generated obstacles

% Generated dirts
% dirt(x, y)
dirt(0, 2).
dirt(1, 2).
dirt(0, 2).

neighbour(X1, X2, Y1,Y2):-
    horizontalNeighbour(X1, X2, Y1,Y2),
    verticalNeighbour(X1, X2, Y1,Y2).

horizontalNeighbour(X1, X2, Y1,Y2):-
    dirt(X1,Y1),
    dirt(X2,Y2),
    /* dont forget to check that 'X' is not equal to 'Y'*/
    X1\=X2.

verticalNeighbour(X1, X2, Y1,Y2):-
    dirt(X1,Y1),
    dirt(X2,Y2),
    /* dont forget to check that 'X' is not equal to 'Y'*/
    X1\=X2.
% Implement search space nodes as coordinate pairs

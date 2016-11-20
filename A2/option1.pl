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
% consult(option1).
%
% But as 'rules' in prolog:

% Grid dimension (fixed):
dim(4).
% Random orientation. ex:

orientation(north).
orientation(east).
orientation(south).
orientation(west).

%% :- dynamic startOrientation/1.

%% startOrientation(I):-
%%     nonvar(I), startOrientation(I)), !; % If it doensn't exist
%%     setof(O, orientation(O),L),
%%     random_member(StartOrientation, L),
%%     retract(),
%%     asserta(startOrientation(StartOrientation)).
startOrientation():-
    setof(O, orientation(O),L),
    random_member(StartOrientation, L),
    asserta(startOrientation(StartOrientation)).

%%     C is random(B),
%% asserta(orientation(O)).
% Randomly choosen robot start position. ex:
startOrientation():-
    setof(O, orientation(O),L),
    random_member(StartOrientation, L),
    asserta(startOrientation(StartOrientation)).

% Generated obstacles
obstacle(0, 3).
obstacle(1, 0).

% Generated dirts
dirts():-
    setof(D, dirt(D),L).

%% :- dynamic dirt/2.
dirt(0, 2).
dirt(1, 2).
dirt(0, 2).

generateDirt(Obstacles) :-
    dim(D), MaxDirt =
    assertz(dirt(X,Y)).


% File generation:
generate :-
    open('names.txt', read, Str),
    my_read(Str,Lines),
    close(Str),
    write(Lines), nl.

my_read(File, L) :-
  read_line_to_codes(File, T),
  (T = end_of_file -> L = [];
  atom_codes(T1, T),
  my_read(File, L1), L = [T1|L1]).


%% neighbour(X1, X2, Y1,Y2):-
%%     /* dont forget to check that 'X' is not equal to 'Y'*/
%%     X1\=X2,
%%     Y1\=Y2,
%%     node(X1, Y1),
%%     node(X2, Y2),
%%     horizontalNeighbour(X1, X2, Y1,Y2),
%%     verticalNeighbour(X1, X2, Y1,Y2).

%% horizontalNeighbour(X1, X2, Y1,Y2):-
%%     dirt(X1,Y1),
%%     dirt(X2,Y2),
%%     /* dont forget to check that 'X' is not equal to 'Y'*/
%%     X1\=X2.

%% verticalNeighbour(X1, X2, Y1,Y2):-
%%     dirt(X1,Y1),
%%     dirt(X2,Y2),
%%     /* dont forget to check that 'X' is not equal to 'Y'*/
%%     X1\=X2.
% Implement search space nodes as coordinate pairs



% Main search predicate
search(X, Y) :-
    write('Move top disk from '),
    write(X),
    write(' to '),
    write(Y),
    nl.

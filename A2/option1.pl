% CLIMATES

% This is a sample of a classification expert system for identification
% of Köppen–Geiger climate types. The rules are roughly based on the classifications
% from https://en.wikipedia.org/wiki/K%C3%B6ppen_climate_classification

% This type of expert system can easily use Prolog's built in inferencing
% system. While trying to satisfy the goal "cat" it tries to satisfy
% various subgoals, some of which will ask for information from the
% user.

% The information is all stored as attribute-value pairs. The attribute
% is represented as a predicate, and the value as the argument to the
% predicate. For example, the attribute-value pair "color-brown" is
% stored "color(brown)".

% "identify" is the high level goal that starts the program. The
% predicate "known/3" is used to remember answers to questions, so it
% is cleared at the beginning of the run.

% The rules of identification are the bulk of the code. They break up
% the problem into identifying process and ingredients and tools before identifying
% the actual climates.

% The end of the code lists those attribute-value pairs which need
% to be asked for, and defines the predicate "ask" and "menuask"
% which are used to get information from the user, and remember it.


main :- identify.

identify:-
  retractall(known(_,_,_)),         % clear stored information
  cat(X),
  write('The climate classification is '),write(X),nl.
identify:-
  write('I can''t identify that climate classification'),nl.

%%% These predicates are required for satisfying the requirements of a higher climate group
%%% They themselves are too broad for the Koppen climate system  
  
rootGroup(a):- % avgTempOfColdestMonth >= 18 C
rootGroup(b):- % avgAnnualPrecipitation < 10 * precipitationThreshold

rootGroupCommon(cd):- % avgTempOfHottestMonth > 10 C

rootGroup(c):- % rootGroupCommon(cd), 0 C < avgTempOfColdestMonth < 18 C
rootGroup(d):- % rootGroupCommon(cd), avgTempOfColdestMonth <= 0 C
rootGroup(e):- % avgTempOfHottestMonth < 10 °C

subGroup(af):- % rootGroup(a), minPrecipitationOfSlowestMonth >= 60mm
subGroup(am):- % rootGroup(a), minPrecipitationOfSlowestMonth < 60mm, (minPrecipitationOfSlowestMonth / totalAnnualPrecipitation) >= 0.04
subGroup(aw):- % rootGroup(a), minPrecipitationOfSlowestMonth < 60mm, (minPrecipitationOfSlowestMonth / totalAnnualPrecipitation) < 0.04

subGroup(bw):- % rootGroup(b), avgAnnualPrecipitation < (5 * precipitationThreshold)
subGroup(bs):- % rootGroup(b), avgAnnualPrecipitation >= (5 * precipitationThreshold)

subGroup(cs):- % rootGroup(c), avgPrecipitationDriestMonthInSummerHalfOfYear < 40mm && avgPrecipitationDriestMonthInSummerHalfOfYear < (avgPrecipitationWettestMonthInWinterHalfOfYear / 3)
subGroup(cw):- % rootGroup(c), avgPrecipitationDriestMonthInWinterHalfOfYear < avgPrecipitationWettestMonthInSummerHalfOfYear / 10
% subGroup(Cf):- % ELIMINATING THIS ONE. This is captured by climate(Cxx) where xx is fa,fb,fc

subGroup(ds):- subGroup(cs). % Same as subGroup(cs)
subGroup(dw):- subGroup(cw). % Same as subGroup(cw)
% subGroup(Df):- % % ELIMINATING THIS ONE. This is captured by climate(Dxx) where xx is fa,fb,fc,fd

subGroup(et):- % rootGroup(e), avgTempOfHottestMonth >= 0C
subGroup(ef):- % rootGroup(e), avgTempOfHottestMonth < 0C

commonRules(cda):- % avgTempOfHottestMonth >= 22 C
commonRules(cdb):- % NOT commonRules(cda), numMonthsWithAvgTempOver10C >= 4
commonRules(cdc):- % NOT commonRules(cdb), NOT commonRules(cdd), 1 <= numMonthsWithAvgTempOver10C < 4
commonRules(cdd):- % avgTempOfColdestMonth < -38C

%%% These predicates are the climate classifications themselves.
%%% They depend on the above rootGroup and subGroup predicates above.

climate(af):- subGroup(af).
climate(am):- subGroup(am).
climate(aw):- subGroup(aw).

climate(bwh):- % subGroup(bw), avgAnnualTemperature >= 18C
climate(bwk):- % subGroup(bw), avgAnnualTemperature < 18C
climate(bs):- subGroup(bs).
climate(bsk):- % subGroup(bs), avgAnnualTemperature < 18C

climate(csa):- % subGroup(cs), commonRules(cda).
climate(csb):- % subGroup(cs), commonRules(cdb).

climate(cwa):- % subGroup(cw), commonRules(cda).
climate(cwb):- % subGroup(cw), commonRules(cdb).
climate(cwc):- % subGroup(cw), commonRules(cdc).

climate(cfa):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cda).
climate(cfb):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cdb).
climate(cfc):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cdc).

climate(dsa):- % subGroup(ds), commonRules(cda).
climate(dsb):- % subGroup(ds), commonRules(cdb).
climate(dsc):- % subGroup(ds), commonRules(cdc).
climate(dsd):- % subGroup(ds), commonRules(cdd).

climate(dwa):- % subGroup(dw), commonRules(cda).
climate(dwb):- % subGroup(dw), commonRules(cdb).
climate(dwc):- % subGroup(dw), commonRules(cdc).
climate(dwd):- % subGroup(dw), commonRules(cdd).

climate(dfa):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cda).
climate(dfb):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cdb).
climate(dfc):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cdc).
climate(dfd):- % (NOT subGroup(cs) && NOT subGroup(cw)), commonRules(cdd).

climate(et):- % subGroup(et).
climate(ef):- % subGroup(ef).

%%% Calculation only predicates

precipitationThreshold():-
% Pthreshold –
% if 70% of precipitation is in winter half of year, 2 × MAT
% if 70% of precipitation is in summer half of year, 2 × MAT + 28
% else 2 × MAT + 14

%%% Predicates requiring user input

minPrecipitationOfSlowestMonth(X):- ask(minPrecipitationOfSlowestMonth,X).
totalAnnualPrecipitation(X):- ask(totalAnnualPrecipitation,X).
avgTempOfColdestMonth(X):- ask(avgTempOfColdestMonth,X).
avgTempOfHottestMonth(X):- ask(avgTempOfHottestMonth,X).
numMonthsWithAvgTempOver10C(X):- ask(numMonthsWithAvgTempOver10C,X).
avgAnnualPrecipitation(X):- ask(avgAnnualPrecipitation,X).
avgAnnualTemperature(X):- ask(avgAnnualTemperature,X).
avgPrecipitationWettestMonthInSummerHalfOfYear(X):- ask(avgPrecipitationWettestMonthInSummerHalfOfYear,X).
avgPrecipitationWettestMonthInWinterHalfOfYear(X):- ask(avgPrecipitationWettestMonthInWinterHalfOfYear,X).
avgPrecipitationDriestMonthInSummerHalfOfYear(X):- ask(avgPrecipitationDriestMonthInSummerHalfOfYear,X).
avgPrecipitationDriestMonthInWinterHalfOfYear(X):- ask(avgPrecipitationDriestMonthInWinterHalfOfYear,X).
  
% "ask" is responsible for getting information from the user, and remembering
% the users response. If it doesn't already know the answer to a question
% it will ask the user. It then asserts the answer. It recognizes two
% cases of knowledge: 1) the attribute-value is known to be true,
% 2) the attribute-value is known to be false.

% This means an attribute might have multiple values. A third test to
% see if the attribute has another value could be used to enforce
% single valued attributes. (This test is commented out below)

% For this system the menuask is used for attributes which are single
% valued

% "ask" only deals with simple yes or no answers. a "yes" is the only
% yes value. any other response is considered a "no".

ask(Attribute,Value):-
  known(yes,Attribute,Value),       % succeed if we know its true
  !.                                % and dont look any further
ask(Attribute,Value):-
  known(_,Attribute,Value),         % fail if we know its false
  !, fail.

ask(Attribute,_):-
  known(yes,Attribute,_),           % fail if we know its some other value.
  !, fail.                          % the cut in clause #1 ensures that if
                                    % we get here the value is wrong.
ask(A,V):-
  write(A:V),                       % if we get here, we need to ask.
  write('? (yes or no): '),
  read(Y),                          % get the answer
  asserta(known(Y,A,V)),            % remember it so we dont ask again.
  Y = yes.                          % succeed or fail based on answer.

% "menuask" is like ask, only it gives the user a menu to to choose
% from rather than a yes on no answer. In this case there is no
% need to check for a negative since "menuask" ensures there will
% be some positive answer.

menuask(Attribute,Value,_):-
  known(yes,Attribute,Value),       % succeed if we know
  !.
menuask(Attribute,_,_):-
  known(yes,Attribute,_),           % fail if its some other value
  !, fail.

menuask(Attribute,AskValue,Menu):-
  nl,write('What is the value for '),write(Attribute),write('?'),nl,
  display_menu(Menu),
  write('Enter the number of choice> '),
  read(Num),nl,
  pick_menu(Num,AnswerValue,Menu),
  asserta(known(yes,Attribute,AnswerValue)),
  AskValue = AnswerValue.           % succeed or fail based on answer

display_menu(Menu):-
  disp_menu(1,Menu), !.             % make sure we fail on backtracking

disp_menu(_,[]).
disp_menu(N,[Item | Rest]):-        % recursively write the head of
  write(N),write(' : '),write(Item),nl, % the list and disp_menu the tail
  NN is N + 1,
  disp_menu(NN,Rest).

pick_menu(N,Val,Menu):-
  integer(N),                       % make sure they gave a number
  pic_menu(1,N,Val,Menu), !.        % start at one
  pick_menu(Val,Val,_).             % if they didn't enter a number, use
                                    % what they entered as the value

pic_menu(_,_,none_of_the_above,[]). % if we've exhausted the list
pic_menu(N,N, Item, [Item|_]).      % the counter matches the number
pic_menu(Ctr,N, Val, [_|Rest]):-
  NextCtr is Ctr + 1,               % try the next one
  pic_menu(NextCtr, N, Val, Rest).
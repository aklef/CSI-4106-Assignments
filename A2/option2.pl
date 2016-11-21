% CLIMATES
info:-
  write('To fix UTF-8 issues: do "Settings" -> "User init file...", add ":- set_prolog_flag(encoding, utf8)."\n\n This is a sample of a classification expert system for identification \n of Köppen-Geiger climate types. The rules are roughly based on the classifications from \n\n https://en.wikipedia.org/wiki/Köppen_climate_classification \n'),
  write("\n This type of expert system can easily use Prolog's built-in inferencing system."),
  write('\n While trying to satisfy the goal "cat", it tries to satisfy various subgoals \n some of which require user input. That information is all stored as attribute-value pairs. \n The attribute is represented as a predicate, and the value as the argument to the predicate. \n\n For example, the attribute-value pair "color-brown" is stored "color(brown)".\n The "identify" predicate is the high level goal that starts the program.'),nl,
  write('\n The predicate "known/3" is used to remember answers to questions, \n so it is cleared at the beginning of the run. \n\n The rules of identification are the bulk of the code. They break up \n the problem into identifying process and ingredients and tools \n before identifying the actual climates. \n\n The end of the code lists those attribute-value pairs which need to be asked for, \n and defines the predicate "ask" and "menuask" which are \n used to get information from the user, and remember it.'),nl,nl.

% Code that runs automatically on load time:
:- use_module(library(clpfd)). % for integer arithmetic
:- info.

main :- identify.

identify:-
  retractall(known(_,_,_)),         % clear stored information
  climate(X),
  write('The climate classification is '),write(X),nl.
identify:-
  write('I can''t identify that climate classification'),nl.

%%% These predicates are required for satisfying the requirements of a higher climate group
%%% They themselves are too broad for the Koppen climate system

rootGroup(a):-
	averageTemperatureOfColdestMonth(ATCM),
	ATCM #>= 18. %°C
rootGroup(b):-
  averageAnnualPrecipitation(AAP),
  precipitationThreshold(PT),
  AAP #< (10 * PT). %mm
rootGroup(c):-
  rootGroupCommon(cd),
  averageTemperatureOfColdestMonth(ATCM),
  0 #< (ATCM #< 18). %°C
rootGroup(d):-
  rootGroupCommon(cd),
  averageTemperatureOfColdestMonth(ATCM),
  ATCM #=< 0. %°C
rootGroup(e):-
  averageTemperatureOfHottestMonth(ATHM),
  ATHM #< 10. %°C
rootGroupCommon(cd):-
  averageTemperatureOfHottestMonth(ATHM),
  ATHM #> 10. %°C

subGroup(af):-
  rootGroup(a),
  averageMinimumPrecipitationOfSlowestMonth(AMPSM),
  AMPSM #>= 60. %mm
subGroup(am):-
  rootGroup(a),
  averageMinimumPrecipitationOfSlowestMonth(AMPSM),
  averageAnnualPrecipitation(AAP),
  AMPSM #< 60, %mm
  (AMPSM div AAP) >= 0.04.
subGroup(aw):-
  rootGroup(a),
  averageMinimumPrecipitationOfSlowestMonth(AMPSM),
  averageAnnualPrecipitation(AAP),
  AMPSM #< 60, %mm
  (AMPSM div AAP) < 0.04.

subGroup(bw):-
  rootGroup(b),
  averageAnnualPrecipitation(AAP),
  precipitationThreshold(PT),
  AAP #< (5 * PT).
subGroup(bs):-
  rootGroup(b),
  averageAnnualPrecipitation(AAP),
  precipitationThreshold(PT),
  AAP #>= (5 * PT).

subGroup(cs):-
  rootGroup(c),
  averagePrecipitationForTheDriestMonthInSummerHalfOfYear(APDMS),
  averagePrecipitationForTheWettestMonthInWinterHalfOfYear(APWMW),
  APDMS #< 40,
  APDMS #< (APWMW div 3).
subGroup(cw):-
  rootGroup(c),
  averagePrecipitationForTheDriestMonthInWinterHalfOfYear(APDMW),
  averagePrecipitationForTheWettestMonthInSummerHalfOfYear(APWMS),
  APDMW #< (APWMS div 10).
% subGroup(Cf):- % ELIMINATING THIS ONE. This is captured by climate(Cxx) where xx is fa,fb,fc

subGroup(ds):-
  subGroup(cs). % Same as subGroup(cs)
subGroup(dw):-
  subGroup(cw). % Same as subGroup(cw)
% subGroup(Df):- % % ELIMINATING THIS ONE. This is captured by climate(Dxx) where xx is fa,fb,fc,fd

subGroup(et):-
rootGroup(e),
  averageTemperatureOfHottestMonth(ATHM),
  ATHM #>= 0. %°C
subGroup(ef):-
  rootGroup(e),
  averageTemperatureOfHottestMonth(ATHM),
  ATHM #< 0. %°C

commonRules(cda):-
  averageTemperatureOfHottestMonth(ATHM),
  ATHM #>= 22. %°C
commonRules(cdb):-
  \+ commonRules(cda),
  averageNumOfMonthsWithAvgTempOverTenDegCelcius(ANMATOTDC),
  ANMATOTDC #>= 4.
commonRules(cdc):-
  \+ commonRules(cdb),
  \+ commonRules(cdd),
  averageNumOfMonthsWithAvgTempOverTenDegCelcius(ANMATOTDC),
  1 #=< (ANMATOTDC #< 4).
commonRules(cdd):-
  averageTemperatureOfColdestMonth(ATCM),
  ATCM #< -38. %°C

%%% These predicates are the climate classifications themselves.
%%% They depend on the above rootGroup and subGroup predicates above.

% Tropical rainforest climate
climate(af):- subGroup(af).
% Tropical monsoon climate
climate(am):- subGroup(am).
% Tropical wet and dry or savanna climate
climate(aw):- subGroup(aw).

% Hot desert
climate(bwh):-
  subGroup(bw),
  averageAnnualTemperature(AAT),
  AAT #>= 18. %°C
% Cold desert
climate(bwk):-
  subGroup(bw),
  averageAnnualTemperature(AAT),
  AAT #< 18. %°C
% Hot steppe
climate(bsh):-
  subGroup(bs),
  averageAnnualTemperature(AAT),
  AAT #>= 18. %°C
% Cold steppe
climate(bsk):-
  subGroup(bs),
  averageAnnualTemperature(AAT),
  AAT #< 18. %°C

% Hot-summer or Mediterranean climates
climate(csa):-
	subGroup(cs),
	commonRules(cda).
% Warm-summer or Mediterranean climates
climate(csb):-
	subGroup(cs),
	commonRules(cdb).
% Cold-summer or Mediterranean climates
climate(csc):-
	subGroup(cs),
	commonRules(cdc).

% Humid subtropical climate
climate(cwa):-
	subGroup(cw),
	commonRules(cda).
% Subtropical highland climate
climate(cwb):-
	subGroup(cw),
	commonRules(cdb).
% Subtropical highland climate
climate(cwc):-
	subGroup(cw),
	commonRules(cdc).

% Humid subtropical climate
climate(cfa):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cda).
% Oceanic climate
climate(cfb):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cdb).
% Subpolar oceanic climate
climate(cfc):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cdc).

% Humid continental climate
climate(dsa):-
	subGroup(ds),
	commonRules(cda).
% Humid continental climate
climate(dsb):-
	subGroup(ds),
	commonRules(cdb).
% Subarctic climate
climate(dsc):-
	subGroup(ds),
	commonRules(cdc).
% Subarctic climate
climate(dsd):-
	subGroup(ds),
	commonRules(cdd).

% Humid continental climate
climate(dwa):-
	subGroup(dw),
	commonRules(cda).
% Humid continental climate
climate(dwb):-
	subGroup(dw),
	commonRules(cdb).
% Subarctic climate
climate(dwc):-
	subGroup(dw),
	commonRules(cdc).
% Subarctic climate
climate(dwd):-
	subGroup(dw),
	commonRules(cdd).

% Humid continental climate
climate(dfa):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cda).
% Humid continental climate
climate(dfb):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cdb).
% Subarctic climate
climate(dfc):-
  (\+ subGroup(cs), \+ subGroup(cw)),
  commonRules(cdc).
% Subarctic climate
climate(dfd):-
(\+ subGroup(cs), \+ subGroup(cw)),
commonRules(cdd).

% Tundra climate
climate(et):-
	subGroup(et).
% Ice cap climate
climate(ef):-
	subGroup(ef).

%%% Calculation only predicates

precipitationThreshold(PT):-
  averageAnnualTemperature(AAT),
  percentageOfAnnualPrecipitationInWinterMonths(PAPWM),
  ( PAPWM >= 0.7 -> PT is (2 * AAT);
    PAPWM =< 0.3 -> PT is (2 * AAT) + 28;
    PT is (2 * AAT) + 14).

/* PT will be the "returned" value
Pthreshold –
if 70% of precipitation is in winter half of year, 2 * MAT
if 70% of precipitation is in summer half of year, (2 * MAT) + 28
else 2 * MAT + 14 */


%%% Predicates requiring user input

percentageOfAnnualPrecipitationInWinterMonths(X):- ask(percentageOfAnnualPrecipitationInWinterMonths,X).
averageMinimumPrecipitationOfSlowestMonth(X):- ask(averageMinimumPrecipitationOfSlowestMonth,X).
averageTemperatureOfColdestMonth(X):- ask(averageTemperatureOfColdestMonth,X).
averageTemperatureOfHottestMonth(X):- ask(averageTemperatureOfHottestMonth,X).
averageNumOfMonthsWithAvgTempOverTenDegCelcius(X):- ask(averageNumOfMonthsWithAvgTempOverTenDegCelcius,X).
averageAnnualPrecipitation(X):- ask(averageAnnualPrecipitation,X).
averageAnnualTemperature(X):- ask(averageAnnualTemperature,X).
averagePrecipitationForTheWettestMonthInSummerHalfOfYear(X):- ask(averagePrecipitationForTheWettestMonthInSummerHalfOfYear,X).
averagePrecipitationForTheWettestMonthInWinterHalfOfYear(X):- ask(averagePrecipitationForTheWettestMonthInWinterHalfOfYear,X).
averagePrecipitationForTheDriestMonthInSummerHalfOfYear(X):- ask(averagePrecipitationForTheDriestMonthInSummerHalfOfYear,X).
averagePrecipitationForTheDriestMonthInWinterHalfOfYear(X):- ask(averagePrecipitationForTheDriestMonthInWinterHalfOfYear,X).

% "ask" is responsible for getting information from the user, and remembering
% the users response. If it doesn't already know the answer to a question
% it will ask the user. It then asserts the answer. It recognizes two
% cases of knowledge: 1) the attribute-value is either known, or it isnt.

% This means an attribute might have multiple values. A third test to
% see if the attribute has another value could be used to enforce
% single valued attributes. (This test is commented out below)

% For this system the menuask is used for attributes which are single
% valued

% "ask" only deals with simple yes or no answers. a "yes" is the only
% yes value. any other response is considered a "no".

:- dynamic known/3.

ask(Attribute,Value):-
  known(valueKnown,Attribute,Value),% succeed if we know its true
  !.                                % and dont look any further
ask(Attribute,Value):-
  known(_,Attribute,Value),         % fail if somehow there is another value besides 'known' or 'yes'
  !, fail.

ask(Attribute,_):-
  known(valueKnown,Attribute,_),    % fail if we know its some other value.
  !, fail.                          % the cut in clause #1 ensures that if
                                    % we get here the value is wrong.
ask(A,V):-
  write('please provide a value for '),
  write(A),                         % if we get here, we need to ask.
  write(': '),
  read(V),                          % get the answer
  asserta(known(valueKnown,A,V)).   % remember it so we dont ask again.
  %Y = .                   % succeed or fail based on answer.

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

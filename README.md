# clojure-dmap

A Clojure port of Will Fitzgerald's small, lisp  dmap project.
https://github.com/willf/lisp_dmap

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## WARNING ##
This is meant as hobby or demonstartion code. Don't adapt it to
a web app. I use slurp and eval, which would be exposing security
issues.

##Introduction
A Direct Memory Access Parser (DMAP)  is a semantic parser, meaning 
the specified patterns are not strictly lexical and can include 
smeantic attributes of the components. Whereas a regular expression
or context free grammar is about combinations of letters, a semantic
parser lets you refer to entries in an ontology, or access memory. 
The research takes this to many deeper levels  not explored here today.
Google Roger Schank, Chris Reisbeck, Will Fitzgerald, Larry Hunter,
and Kevin Livingston  for relevant papers.

This implementation started with a lisp project Wil Fitzgerald wrote.
The goals here are not to produce a perfect translation, or to advance
research, but to learn clojure by attemtping to do something similar.

## Components

###Patterns
The patterns don't involve regular expressions and are simply lists
of words to be matched verbatim. Patterns are distiguished from
ontology references by the use of symbol notation: a colon prefix.
For example "Chris uses :vehicle transportation to commute to work."
would refer to an ontology class :vehicle that that includes bus, 
train, pedestrian, bicycle or car.

###Ontology Entries
In this vresion the ontology is kept in frames. These are structure
with attributes called slots and specialization (derivation) and
abstraction (base class) links to other frames. There is special
code for this rather than defstructs so they can be dynamically
created. The symbols used in patterns are names of entries here.

###Search Algorithm
Given a token in the input stream, the matcher looks for patterns
that either start with that token, or that have been started and
are waiting for this token as the next part of the pattern. The
rules are search for start words, and a list of rules in progress
is maintained in a hash keyed by the next required token.

##Future Work
- add regular expressions
- be able to layer rules for better rule modularity
- expand the syntax for easier rule writing: optional sections,
  references to previously matched portions
- find ways to extract rules from a corpus of documents along
the lines of Autoslog or Sundance projects by Ellen Riloff in Utah.
- apply the ability to refer to ontologies to the output of
grammar parsers on the nput text. Instead of just matching 
letters or for complete matches on an ontology, you could 
specify the grammatical type associated with a token
- continue with parsers and pursue dependency parse information
for building thematic role assignments  
- switch from a frames representation for the ontology to 
predicate-based, perhaps in a triple store
- use same for the patterns and internal state
- expand the use of memory as Kevin Livingston has doen to
include episodic memory linking phrases to build context.
This would allow you to find the inter-sentence meaning in
a pair of sentences like "Chris is on the bus. He is going
to work."
- explore the use of reasoning engines on triple stores
to accomplish this

Clearly more opportunity than a hobby requires.

##Applications
The examples in Fitzgerald's work show natural language
understanding applied to customer service representative
scripts. Larry Hunter et al  have applied dmap in an 
implementation applied to information extraction from
biomedical research papers with a project called OpenDMAP. 
It has seen high levels of precision in community shared
tasks such as BioCreative III and others.  Kevin Livingston and 
Larry Hunter are continuing this work using large
ontologies. It may play a role in building models of
thematic roles in domain-specific verb usage.


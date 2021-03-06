# clojure-dmap

Clojure work inspired by  Will Fitzgerald's small, lisp  dmap project.
https://github.com/willf/lisp_dmap

More info here from Chris Riesbeck: http://www.cs.northwestern.edu/academics/courses/325/index.php

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## WARNING ##
This is meant as hobby or demonstration code. I'm learning both Clojure and the details of simple DMAP implementations by writing this. Don't pick up my bad habbits.  Don't adapt it to a web app as I might use slurp and eval, which would be exposing security
issues.

## Quick Start
Assuming Leiningen is availabe: lein run
Have a look at 
- src/clojure_dmap/frames.clj for the ontology of concepts, 
- src/clojure_dmap/patterns.clj for concept patterns, 
- and finally src/clojure_dmap/dmap.clj for the text strings analyzed.

## TODO
- use real logging
- fill matched slots
- some of the rules aren't created with the usual function
- some tests are commented out in frame.clj

##Introduction
A Direct Memory Access Parser (DMAP)  is a semantic parser, meaning the specified patterns are not strictly lexical and include smeantic attributes of the components. Whereas a regular expression or context free grammar is about combinations of letters (words)  and combinations of those combinations (phrases), a semantic parser lets you write patterns in terms of concepts rather than the words. At first blush this allows for synonyms, but the ONTOLGOY....  just represenations of concepts. The patterns are generalized, written using ids or names of concepts not just one refer to entries in an ontology, or access memory. 

The research takes this to many deeper levels  not explored here today.  Google Roger Schank, Chris Reisbeck, Will Fitzgerald, Larry Hunter, and Kevin Livingston  for relevant papers.

This implementation started with a lisp project Wil Fitzgerald wrote.  The goals here are not to produce a perfect translation, or to advance research, but to learn clojure by attemtping to do something similar.

## Components

###Patterns
The patterns don't involve regular expressions and are simply lists of words to be matched verbatim. Patterns are distiguished from ontology references by the use of symbol notation: a colon prefix.  For example "Chris uses :vehicle transportation to commute to work." would refer to an ontology class :vehicle that that includes bus, train, pedestrian, bicycle or car.

There are also more complicatd patterns that assemble sentencs from the words or concepts in the more simple patterns mentioned above.

### Frames or Ontology Entries
In this vresion the ontology is kept in frames. These are structure with attributes called slots and specialization (derivation) and abstraction (base class) links to other frames. There is special code for this rather than defstructs so they can be dynamically created. The symbols used in patterns are names of entries here. In the examples here , a frame usually contains information very similar to what a single sentence would hold. There's lots to be said about ontology design, and more to be said about how to harvest the information across sentences.

###Search Algorithm
Given a token in the input stream, the matcher looks for patterns that either start with that token, or that have been started and are waiting for this token as the next part of the pattern. The rules are search for start words, and a list of rules in progress is maintained in a hash keyed by the next required token.

#### filling the frames...as the rules are finished...

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
The examples in Fitzgerald's work show natural language understanding applied to customer service representative scripts. Larry Hunter et al  have applied dmap in an implementation applied to information extraction from biomedical research papers with a project called OpenDMAP.  It has seen high levels of precision in community shared tasks such as BioCreative III and others.  Kevin Livingston and Larry Hunter are continuing this work using large ontologies. It may play a role in building models of thematic roles in domain-specific verb usage.


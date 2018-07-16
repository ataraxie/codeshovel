# CodeShovel Paper Outline

# Introduction

## What is the problem?
* Software systems evolve and source code is THE THING to trace this (cite!)
* Every development project somewhat professional uses version control (cite!)
* But we don't really know how developers use version control
* We suspect that the prevalent file-based view on history is not enough! (esp. with refactoring, moving of methods, etc.)

## Sample scenario
* Consider the following sample scenario that we have found to be a common problem among developers
* A developer is about to review a pull request that changed a method
* She has not seen this particular segment of code in a while and lacks understanding of what the code is doing
* Since documentation is insufficient for a clear understanding and the author of the pull request is unavailable she decides to investigate in the version history of this method
* She uses her version control tool to show the history of the file that contains the method
* But the file has hundreds of commits and only a few changed this method
* She decides to look at the history of the line range of this method (or e.g.: "Show history for method/selection" in IntelliJ)
* But the first commit that shows a change to the method is only a month old and she doubts that this commit really introduced the method
* Clearly, tracing the history of this method further would require enormous effort "digging" through the source code history of potentially many different files (if there has been refactoring in the project)

## What do we do in this paper?
* With an extensive survey with X professional devs, we analyze:
  * How do devs use source code history? What tools? What views?
  * How hard is it to find neccessary information for given tasks with the current tools?
  * How can these tasks be simplified with a more semantic view on the history of dedicated code units?
* We describe our prototype tool CodeShovel that
  * Provides this semantic view on Java and JavaScript methods (as the code unit devs are more interested in as the survey shows)
  * The tool specifically continues methods' histories beyond refactoring tasks like moving to a different file (as the modification tasks devs in the survey said was the hardest to follow with current tools)
* We evaluate our tool with...?
  * (NOT SURE WHAT TO WRITE HERE. HERE'S SOME NOTES:)
    * All history tools somehow use git log as the bases for the history of (1) files, (2) line ranges / selections, (3) code units (IntelliJ has a "Show history of method" function that basically just uses the line range of the method)
    * We use git log with the line range as a base line since this is what all the tools use
    * We compare the results of CodeShovel against it
    * We find that:
      * the baseline shows many changes that have nothing to do with the method
      * the baseline doesn't show changes that have to do with the method a
      * all missing information is found with CodeShovel
      * wrong information is not found with CodeShovel

## What are our concrete contributions?

* An extensive survey with X professional developers on their usage of source code history, showing that there is a lack of support for the history of semantic code units.
* CodeShovel, a source code history tool that provides a semantic view on the history of dedicated methods in Java and JavaScript.
* A mixed methods evaluation of this approach showing its accuracy and advantages and how it can be used in the future.

## Research Questions

(PROBABLY THESE SHOULDN'T END UP IN THE INTRODUCTION IN THIS CASE BUT IT'S GOOD TO HAVE THEM HERE. THESE ARE JUST COPIED FROM THE GOOGLE DOC FOR THE SURVEY!)

RQ1: Do developers use source code history when they are working with code? If so, how?
  * When they try to understand some pieces of code, do they use the history at all?
  * If they do, how do they use it? What tools? What views?
RQ2: What are developers trying to learn when they examine source code history?
  * What specific questions are they trying to answer?
  * What information are they seeking? 
  * What do they search for? 
RQ3: In terms of their mental models and information needs, what level of temporal and structural granularity are most appropriate when using source code history?
  * What is the temporal granularity (e.g., what are the timeframes they are interested in?)
  * What is the structural granularity (e.g., what is the scope of code they are interested in?) 
    * Possibilities: [directory (and children)-level, package-level, file-level, class-level, field-level, method-level, block-level]
RQ4: Does augmenting history with semantic data improve program comprehension?
How effectively can a semantically-aware code history viewer support program comprehension?
  * History tools and views are currently mainly based on the views of a file system and on the textual representation of code. Could we support program comprehension better by providing a semantic view on the history of code units rather than such a simplistic textual/file system view?
  * (Idea/Approach: step through Git history, build ASTs for each revision, match changes with semantic units, provide natural interpretation)

## Structure of the paper

(I PERSONALLY DON'T LIKE THESE PARAGRAPHS BUT I THINK ALL ICSE PAPERS HAVE THEM?)

Section 1... section 2... section 3...

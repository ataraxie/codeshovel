(Section 1: general questions)

The history of source code can help developers greatly in following and understanding code changes over time. While most version control tools provide efficient ways to browse through the changes of a file, there is yet no widespread way to browse the history of only specific code constructs like modules and functions. In practice, source code files mostly consist of many such building blocks and it might be valuable to analyze the history of these blocks in a more isolated fasion.

The intention behind this survey is to find out whether and how developers currently analyze the history of specific code constructs and how this analysis can be simplified. For the following questions, imagine the following scenario: you are reviewing a change to a JavaScript function in a pull request. You want to get a better picture about this function and its relevance, and understand what led to this change.

Q1: How would you approach this? Please describe briefly.
(free text)

Q2: Would it be likely for you to use version control history?
(not likely <-> very likely)

Q3: If so, how would you find changes to this function?
(free text)

Q4: Would it help if a tool reported you all changes only specific to this function?
(not helpful <-> very helpful)

======

(Section 2: specific scenario)

Now, imagine yourself on the jQuery dev team. You are to review a pull request with a change on the method `jQuery.data` in `src/data.js` as seen in [this commit](https://github.com/jquery/jquery/commit/64a289286a743516bce82462200062a647ef3ac0#diff-be99d06c129c1d2d7d05e3876e275e21). In order to review this pull request, you want to get a better picture on how this method has changed over the past. You decide to look into the history of the file `src/data.js` on Github as seen [here](https://github.com/jquery/jquery/commits/master/src/data.js).

While Github will make it easy for you to browse through the history of `src/data.js` (181 revisions in 10 years), it won't allow you to browse only the changes made to the `jQuery.data` method.

Q5: Would you find it helpful if there were an option "Show function history" on pull request diffs as the one above?
(not helpful <-> very helpful)
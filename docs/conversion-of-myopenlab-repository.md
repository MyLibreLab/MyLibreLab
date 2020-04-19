# Conversion of MyOpenLab repository

Following steps were done to convert the SVN-based MyOpenLab repository to this repository

- git to svn conversion of <https://sourceforge.net/p/myopenlab3/code/HEAD/tree/>
  
  - using publicly availble information of the authors Carmelo Salafia and Javier Velásquez
  - not used flag `--stdlayout`, since there were no branches and the tags "3.9.0" and "3.11.0" did not correspond to commits in the `trunk`
  
- Remove unwanted binary files from the code base using [git bfg-ish](https://github.com/newren/git-filter-repo/blob/master/contrib/filter-repo-demos/bfg-ish)

  - find out all extensions: `git log --name-only --oneline | grep -v  "........ " | sed "s/.*\.\(\[^.\]\)*/\1/" | grep -v "/" | sort | uniq > all-extensions`
  - `*.~*`
  - `*.{class,jar,bak}`
  - complete `distribution` folder

- Restructore repository to follow typical maven-project structure

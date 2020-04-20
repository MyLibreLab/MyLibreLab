# Conversion of MyOpenLab repository

Following steps were done to convert the SVN-based MyOpenLab repository to this repository

- git to svn conversion of <https://sourceforge.net/p/myopenlab3/code/HEAD/tree/>
  - using availble information (in the code) of the authors Carmelo Salafia and Javier Velásquez. Put in `authors.txt`

      ```properties
      rjvelasquez40 = Javier Velásquez <javiervelasquez125@gmail.com>
      carmelosalafia = Carmelo Salafia <cswi@gmx.de>
      ```

  - not used flag `--stdlayout`, since there were no branches and the tags "3.9.0" and "3.11.0" did not correspond to commits in the `trunk`
  - Intermediate result published at <https://github.com/MyLibreLab/MyOpenLab>
- Remove unwanted binary files from the code base using [git bfg-ish](https://github.com/newren/git-filter-repo/blob/master/contrib/filter-repo-demos/bfg-ish)
  - find out all extensions: `git log --name-only --oneline | grep -v  "........ " | sed "s/.*\.\(\[^.\]\)*/\1/" | grep -v "/" | sort | uniq > all-extensions`
  - Remove backup files `*.~*`
  - Remove binary files `*.{class,jar,bak,a,dll,o,so,jnilib,exe,zip}`
  - Remove Mac OS X files `*DS_Store`

    ```terminal
    python C:\Python38\Scripts\bfg-ish.py --delete-files "*.{class,jar,bak,dll,so,exe,zip,jnilib}" .
    ```

  - `distribution/elements` folder moved to separate repository <https://github.com/MyLibreLab/elements>
    - Preparation: `git filter-branch --prune-empty --subdirectory-filter distribution/elements`
- Restructore repository to follow typical maven-project structure

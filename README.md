# ksp-mission-fixer

Fixes broken KSP repair/part contracts. Essentially just the procedure outlined 
on [this reddit post](https://old.reddit.com/r/KerbalAcademy/comments/kgh4pm/new_repair_rover_mission_not_working/gqbi1d2)

## Usage
* clone project
* gradle build
* run this command
```
  java -cp build\libs\kspMissionFixer-1.0-SNAPSHOT.jar com.semarware.ksp.Fixer "\Program Files (x86)\Steam\steamapps\common\Kerbal Space Program\saves\career_expansion\persistent.sfs
```

This will create a new file called persistent.sfs.fixed. After confirming that it is correct, copy that file over your persistent.sfs start up KSP.

Note: this will only succeed if all of these are true:
* It finds missions with vessel id reference 
* The mission vessel id does not point to any other vessels 
* The mission mentions a vessel by name and vessel with that name actually exists
<#
.SYNOPSIS
Builds the project, source and javadoc artefacts, signs them and generates an artefact bundle to be uploaded to
Nexus. Requires 7-zip and GnuPG to be on the PATH.
#>

$signUser = Read-Host -Prompt 'User ID for signing'

$gradle = '../gradlew.bat'
$buildDir = './build'

$packageDir = "$buildDir/package"
$libsDir = "$buildDir/libs"
$javadocBuildDir = "$buildDir/dokka/javadoc"

'Remove build directory' | Out-Host
Remove-Item -Recurse -Force ./build -ErrorAction SilentlyContinue

'Build project, generate Javadoc' | Out-Host
& $gradle clean build dokkaJavadoc

'Make package directory' | Out-Host
mkdir $packageDir

'Copy build artefacts to package directory'
Copy-Item "$libsDir/*" $packageDir

'Generate POM' | Out-Host
& $gradle generatePomFileForSkgl-kotlinPublication
Copy-Item "$buildDir/publications/skgl-kotlin/pom-default.xml" "$packageDir/$((Get-ChildItem "$packageDir/*sources.jar").Name -replace '\-sources\.jar').pom"

'Assembling Javadoc archive' | Out-Host
7z a "$packageDir/$((Get-ChildItem "$packageDir/*sources.jar").Name -replace 'sources\.jar$','javadoc.jar')" "$javadocBuildDir/**"

'Signing' | Out-Host

Push-Location .
Set-Location $packageDir

Get-ChildItem | ForEach-Object {
	gpg --local-user $signUser --armor --detach-sign $_.Name
}

'Creating bundle' | Out-Host
jar -cvf bundle.jar (Get-ChildItem)

Pop-Location

'Done' | Out-Host
Get-ChildItem $packageDir

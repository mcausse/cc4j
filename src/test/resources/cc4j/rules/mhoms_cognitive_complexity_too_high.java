import java.util.*;

public class Jou {

    @Override
    public void upgrade3(ArtifactsProcessStatus status) {

        Optional<ArtifactVersion> currentVersion = process.getCurrentInstalledApacheVersionFromRegistry();

        if (currentVersion.isEmpty()) {
            logger.error("Missing Apache on the pc. Go to navify PLH install.");
            throw new InstallerException("Missing Apache on the pc. Go to navify PLH install.");
        } else if (currentVersion.get().isLowerThan(getArtifactVersion())) {
            logger.info("Current installed version is: [{}], witch is lower than the candidate to install: [{}], " +
                    "so assuming that an upgrade is required.", currentVersion.get(), getArtifactVersion());
            process.upgradeApache();
        } else if (getArtifactVersion().isLowerThan(currentVersion.get())) {
            logger.error("Higher version of Apache, the current version is {} and the Installer version is {}.",
                    currentVersion, getArtifactVersion());
            throw new InstallerException("Higher version of Apache. Installer version (" + getArtifactVersion()
                    + "). Current version (" + currentVersion.get() + ")");
        } else {
            logger.info(
                    "Same version of Apache, it's not needed to upgrade it. Current version ({}) and installer version: ({}).",
                    currentVersion.get(), getArtifactVersion());
        }
    }

    @Override
    public void install4(ArtifactsProcessStatus status) {
        Optional<ArtifactVersion> currentVersion = openJDKProcess.getCurrentInstalledOpenJDKVersionFromInstallerArguments();

        if (currentVersion.isPresent() && getArtifactVersion().isLowerThan(currentVersion.get())) {
            logger.error("Higher version of OpenJDK, the current version is {} and the Installer version is {}.", currentVersion, getArtifactVersion());
            throw new InstallerException("Higher version of OpenJDK. Installer version (" + getArtifactVersion() + "). Current version (" + currentVersion.get() + ")");
        } else if (currentVersion.isPresent() && currentVersion.get().equals(getArtifactVersion())) {
            logger.info("Same version of OpenJDK, it's not needed to install it. Current version ({}) and installer version: ({}).", currentVersion.get(), getArtifactVersion());
            return;
        }

        logger.info("Needed version can not be loaded, so it is assumed that OpenJDK is not installed, and a fresh installation is required.");

        openJDKProcess.installOpenJDK();
    }

    protected Map<String, Object> findOrCreateNode6(String nodesPath) {
        if (nodesPath.isEmpty()) {
            return yamlTree;
        }
        String[] nodesPathParts = nodesPath.split("\\.");
        Map<String, Object> yamlNode = yamlTree;
        for (String propertyPathPart : nodesPathParts) {
            if (!yamlNode.containsKey(propertyPathPart)) {
                yamlNode.put(propertyPathPart, new LinkedHashMap<String, Object>());
            } else if (!(yamlNode.get(propertyPathPart) instanceof Map)) {
                // is not a node
                throw new InstallerException("the Yaml node '" + propertyPathPart + "' exists as a leaf (with value='" +
                        yamlNode.get(propertyPathPart) + "'); evaluating " + nodesPath);
            }
            yamlNode = (Map<String, Object>) yamlNode.get(propertyPathPart);
        }
        return yamlNode;
    }

    public void next9() {
        List<RocheInstallCheckBox> cards = checkboxesCard.getRiCheckBoxesList();
        for (RocheInstallCheckBox card : cards) {
            if (card.getIsChecked()) {
                if (card.getType() == RocheInstallCheckBox.RocheInstallCheckBoxType.UNINSTALL) {
                    screensMediator.showUninstallScreen();
                    screensMediator.launchUninstallationProcess();
                }
                if (card.getType() == RocheInstallCheckBox.RocheInstallCheckBoxType.SECURITY) {
                    screensMediator.showSetUpSecuritySettingScreenWithAuthentication();
                    return;
                }
                screensMediator.showSetUpScreen(card.getType().getProcessType());
                checkboxesCard.unselectAllCards();
                twoButtonsPanel.setSecondButtonStatus(false);
                return;
            }
        }
    }

    protected Object convertArgumentToType6(String propValue, Class<?> propType) {
        final Object value;
        if ("null".equals(propValue)) {
            value = null;
        } else if (propType.equals(int.class) || propType.equals(Integer.class)) {
            value = Integer.valueOf(propValue);
        } else if (propType.equals(boolean.class) || propType.equals(Boolean.class)) {
            value = Boolean.valueOf(propValue);
        } else if (propType.isAssignableFrom(Map.class)) {
            value = stringToMap(propValue);
        } else {
            value = propValue;
        }
        return value;
    }

}
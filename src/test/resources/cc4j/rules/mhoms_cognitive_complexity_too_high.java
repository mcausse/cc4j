import com.sun.source.tree.*;
import org.homs.cc4j.util.CodeCleaner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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

    public String getValue18(String positionExpression) {
        var m42exp = M42PositionExpression.build(positionExpression);
        int segmentIndex = findSegmentIndex(m42exp);
        if (m42exp.position == null) {
            return m4.getValue(segmentIndex);
        } else {
            var position = m42exp.position;
            /*
             * MSH Position Corection
             *
             *  MSH.0 - Segment name            MSH
             *  MSH.1 - Field Separator         |
             *  MSH.2 - Encoding Characters     ^~\&
             *  MSH.3 - Sending Application     Sopa
             */
            if (m42exp.segmentName.equals("MSH")) {
                if (position == 0) {
                    return "MSH";
                } else if (position == 1) {
                    // m4.getSeparators()[0] és \n
                    return String.valueOf(m4.getSeparators()[1]);
                } else if (position == 2) {
                    var separators = new StringBuilder();
                    for (int i = 2; i < m4.getSeparators().length; i++) {
                        separators.append(m4.getSeparators()[i]);
                    }
                    return separators.toString();
                } else {
                    position--;
                }
            }

            if (m42exp.subposition == null) {
                return m4.getValue(segmentIndex, position);
            } else {
                return m4.getValue(segmentIndex, position, m42exp.subposition);
            }
        }
    }

    public Map<String, String> CucarachaMicroDb_find11(Predicate<String> keyPredicate, Predicate<String> valuePredicate) {
        Map<String, String> r = new LinkedHashMap<>();
        for (Object okey : p.keySet()) {
            String key = (String) okey;
            if (keyPredicate == null || keyPredicate.test(key)) {
                String value = p.getProperty(key);
                if (valuePredicate == null || valuePredicate.test(value)) {
                    r.put(key, value);
                }
            }
        }
        return r;
    }

    public String getVentanaMessageType9(M42 m42) {
        String messageType = getMessageType(m42);
        if (messageType.equals("OML^O21")) {
            if (isWorkflowFlexibilityRequest(m42)) {
                return "OE/WF";
            } else if (isAdditionRequest(m42)) {
                return "Addition";
            }
        } else if (messageType.equals("OUL^R21")) {
            if (m42.existsSegment("OBR")) {
                var artifactType = m42.getValue("OBR-4-0");
                if (artifactTypes.contains(artifactType)) {
                    return m42.getValue("OBR-4");
                }
            }
        }
        return messageType;
    }

    CodeCleaner.State consumeChar25(CodeCleaner.State state, char c, StringBuilder strb) {
        switch (state) {
            case CODE -> {
                strb.append(c);
                if (c == '/') {
                    return CodeCleaner.State.SLASH;
                }
                if (c == '"') {
                    return CodeCleaner.State.STRING;
                }
                return CodeCleaner.State.CODE;
            }
            case SLASH -> {
                if (c == '/') {
                    strb.append(c);
                    return CodeCleaner.State.SINGLELINE_COMMENT;
                }
                if (c == '*') {
                    strb.append(c);
                    return CodeCleaner.State.MULTILINE_COMMENT;
                }
                strb.append(encode(c));
                return CodeCleaner.State.CODE;
            }
            case SINGLELINE_COMMENT -> {
                if (c == '\n') {
                    strb.append(c);
                    return CodeCleaner.State.CODE;
                }
                if (cleanComments) {
                    strb.append(encode(c));
                } else {
                    strb.append(c);
                }
                return CodeCleaner.State.SINGLELINE_COMMENT;
            }
            case MULTILINE_COMMENT -> {
                if (c == '*') {
                    strb.append(c);
                    return CodeCleaner.State.STAR;
                }
                if (cleanComments) {
                    strb.append(encode(c));
                } else {
                    strb.append(c);
                }
                return CodeCleaner.State.MULTILINE_COMMENT;
            }
            case STAR -> {
                if (c == '/') {
                    strb.append(c);
                    return CodeCleaner.State.CODE;
                }
                if (c == '*') { // while *
                    strb.append(c);
                    return CodeCleaner.State.STAR;
                }
                strb.append(encode(c));
                return CodeCleaner.State.MULTILINE_COMMENT;
            }
            case STRING -> {
                if (c == '"') {
                    strb.append(c);
                    return CodeCleaner.State.CODE;
                }
                if (cleanStrings) {
                    strb.append(encode(c));
                } else {
                    strb.append(c);
                }
                return CodeCleaner.State.STRING;
            }
            default -> throw new RuntimeException(state.name());
        }
    }


    int inspectStatement20(StatementTree stm, int level) {
        if (stm instanceof BlockTree) {
            return inspectStatements(((BlockTree) stm).getStatements(), level);
        } else if (stm instanceof DoWhileLoopTree) {
            return inspectStatement(((DoWhileLoopTree) stm).getStatement(), level);
        } else if (stm instanceof ForLoopTree) {
            return inspectStatement(((ForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof EnhancedForLoopTree) {
            return inspectStatement(((EnhancedForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof IfTree) {
            return Math.max(
                    inspectStatement(((IfTree) stm).getThenStatement(), level),
                    inspectStatement(((IfTree) stm).getElseStatement(), level));
        } else
//        if (stm instanceof LambdaExpressionTree) {
//            inspectStatement(issuesReport, location, ((LambdaExpressionTree) stm).getBody().);
//        }else
            if (stm instanceof SwitchTree) {
                var cases = ((SwitchTree) stm).getCases();
                int localLevel = level;
                for (var casee : cases) {
                    if (casee.getStatements() != null) {
                        int caseMaxLevel = inspectStatements(casee.getStatements(), level);
                        if (localLevel < caseMaxLevel) {
                            localLevel = caseMaxLevel;
                        }
                    }
                }
                return localLevel;
            } else if (stm instanceof TryTree) {
                var stmTree = (TryTree) stm;
                int maxLevel = inspectStatements(stmTree.getBlock().getStatements(), level);
                for (CatchTree catchTree : stmTree.getCatches()) {
                    int catchMaxLevel = inspectStatement(catchTree.getBlock(), level);
                    if (maxLevel < catchMaxLevel) {
                        maxLevel = catchMaxLevel;
                    }
                }
                if (stmTree.getFinallyBlock() != null) {
                    int finallyMaxLevel = inspectStatements(stmTree.getFinallyBlock().getStatements(), level);
                    if (maxLevel < finallyMaxLevel) {
                        maxLevel = finallyMaxLevel;
                    }
                }
                return maxLevel;
            } else if (stm instanceof WhileLoopTree) {
                return inspectStatement(((WhileLoopTree) stm).getStatement(), level);
            } else {
                // XXX és l'indent level fulla, però no té pq ser el més fondo!!!
                return level;
            }
    }
}
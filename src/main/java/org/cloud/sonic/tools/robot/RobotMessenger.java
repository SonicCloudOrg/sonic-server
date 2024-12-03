/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.tools.robot;

import org.cloud.sonic.controller.tools.robot.message.DeviceMessage;
import org.cloud.sonic.controller.tools.robot.message.ProjectSummaryMessage;
import org.cloud.sonic.controller.tools.robot.message.TestSuiteMessage;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.web.client.RestTemplate;

import java.util.WeakHashMap;

public interface RobotMessenger {
    ExpressionParser defaultParser = new SpelExpressionParser();
    ParserContext templateParserContext = new TemplateParserContext();
    WeakHashMap<String, Expression> expressionCache = new WeakHashMap<>();
    EvaluationContext ctx = SimpleEvaluationContext.forReadOnlyDataBinding().withInstanceMethods().build();

    static Expression parseTemplate(String template) {
        return expressionCache.computeIfAbsent(template, it -> defaultParser.parseExpression(it, templateParserContext));
    }

    default void sendMessage(RestTemplate restTemplate, String token, String secret, String messageTemplate, Message message) {
        Expression template;
        if (messageTemplate.isEmpty()) {
            if (message instanceof TestSuiteMessage) {
                template = getDefaultTestSuiteTemplate();
            } else if (message instanceof ProjectSummaryMessage) {
                template = getDefaultProjectSummaryTemplate();
            } else if (message instanceof DeviceMessage) {
                template = getDefaultDeviceMessageTemplate();
            } else {
                return;
            }
        } else {
            template = parseTemplate(messageTemplate);
        }
        sendMessage(restTemplate, token, secret, template, message);
    }

    void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message message);

    Expression getDefaultTestSuiteTemplate();

    Expression getDefaultProjectSummaryTemplate();

    Expression getDefaultDeviceMessageTemplate();
}

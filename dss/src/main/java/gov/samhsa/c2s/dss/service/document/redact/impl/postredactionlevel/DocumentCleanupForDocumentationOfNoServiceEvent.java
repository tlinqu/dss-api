/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the <organization> nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.c2s.dss.service.document.redact.impl.postredactionlevel;

import gov.samhsa.c2s.brms.domain.FactModel;
import gov.samhsa.c2s.brms.domain.RuleExecutionContainer;
import gov.samhsa.c2s.brms.domain.XacmlResult;
import gov.samhsa.c2s.common.document.accessor.DocumentAccessor;
import gov.samhsa.c2s.common.document.accessor.DocumentAccessorException;
import gov.samhsa.c2s.common.log.Logger;
import gov.samhsa.c2s.common.log.LoggerFactory;
import gov.samhsa.c2s.dss.service.document.dto.RedactionHandlerResult;
import gov.samhsa.c2s.dss.service.document.redact.RedactionHandlerException;
import gov.samhsa.c2s.dss.service.document.redact.base.AbstractPostRedactionLevelRedactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.stream.Stream;

/**
 * The Class DocumentCleanupForDocumentationOfNoServiceEvent.
 */
@Service
public class DocumentCleanupForDocumentationOfNoServiceEvent extends
        AbstractPostRedactionLevelRedactionHandler {

    /**
     * The Constant XPATH_DOCUMENTATIONOF_WITH_NO_SERVICE_EVENT.
     */
    public static final String XPATH_DOCUMENTATIONOF_WITH_NO_SERVICE_EVENT = "/hl7:ClinicalDocument/hl7:documentationOf[not(hl7:serviceEvent)]";
    private final Logger logger = LoggerFactory.getLogger(this);

    /**
     * Instantiates a new document cleanup for documentation of no service
     * event.
     *
     * @param documentAccessor the document accessor
     */
    @Autowired
    public DocumentCleanupForDocumentationOfNoServiceEvent(
            DocumentAccessor documentAccessor) {
        super(documentAccessor);
    }

    @Override
    public void execute(Document xmlDocument, XacmlResult xacmlResult,
                        FactModel factModel, Document factModelDocument,
                        RuleExecutionContainer ruleExecutionContainer,
                        RedactionHandlerResult preRedactionResults) {
        try {
            Stream<Node> documentationOfElements = documentAccessor.getNodeListAsStream(
                    xmlDocument, XPATH_DOCUMENTATIONOF_WITH_NO_SERVICE_EVENT);
            documentationOfElements.forEach(this::nullSafeRemove);
        } catch (DocumentAccessorException e) {
            throw new RedactionHandlerException(e);
        }
    }
}

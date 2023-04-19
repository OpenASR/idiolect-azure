package org.openasr.idiolect.asr.azure

import com.intellij.ui.dsl.builder.*
import javax.swing.*

class AzureSettingsForm {

    internal val subscriptionKey = JTextField()
    internal val serviceRegion = JTextField()

    fun reset() {
        subscriptionKey.text = AzureConfig.settings.speechSubscriptionKey
        serviceRegion.text = AzureConfig.settings.serviceRegion
    }

    internal val rootPanel: JPanel = panel {
        group("Model") {
            row { browserLink("Azure documentation", "https://learn.microsoft.com/en-us/azure/cognitive-services/speech-service/get-started-speech-to-text?tabs=windows%2Cterminal&pivots=programming-language-csharp#set-up-the-environment") }
            row("Subscription key") { cell(subscriptionKey).columns(COLUMNS_SHORT) }
            row("Service region") { cell(serviceRegion).columns(COLUMNS_SHORT) }
        }
    }
}

# ![idiolect icon](src%2Fmain%2Fresources%2FMETA-INF%2FpluginIcon.svg) idiolect-azure

While the [idiolect](https://github.com/OpenASR/idiolect) IntelliJ plugin uses the Vosk off-line speech recognition service which
supports several different voice models for EN-US, EN-UK and others (and also custom models), it's performance for some 
users may not be very high. 
[Azure's Cognitive Services](https://learn.microsoft.com/en-us/azure/cognitive-services/speech-service/get-started-speech-to-text?tabs=windows%2Cterminal&pivots=programming-language-csharp#set-up-the-environment)
requires an Azure subscription, internet access and may incur a $ cost.

Azure provide a [free tier](https://azure.microsoft.com/en-au/pricing/details/cognitive-services/speech-services/) with **5 hours of per month**.

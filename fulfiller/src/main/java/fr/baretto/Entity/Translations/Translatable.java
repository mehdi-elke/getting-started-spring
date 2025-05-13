package fr.baretto.Entity.Translations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public interface Translatable {
    @JsonProperty("translations")
    Map<String, String> getTranslations();
    
    List<? extends TranslatableEntity> getTranslationEntities();
} 
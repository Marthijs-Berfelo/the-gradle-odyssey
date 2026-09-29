---
transition: slide-up
---

# <StageIcon name="laurel" :size="40" /> Ithaca, Regained

<WaveDivider />

Published, shared plugin — home again, transformed

<br/>

`buildSrc` becomes a published plugin — reusable far beyond this repo.

<!--
The final transformation: buildSrc's convention plugins are extracted into their own
standalone Gradle plugin project, build-logic-plugin, published to mavenLocal(). Instead of
copying build logic into every new repo, heroes-service and monsters-service simply apply
it via plugins { id(...) } — versioned through the same libs.versions.toml catalog.

This is the payoff for the whole journey: what started as a single module's build.gradle.kts
is now build logic any project can adopt, with no copying and no drift.
-->

---
transition: slide-up
---

<StageFooter icon="laurel" name="Ithaca, Regained" />

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {scale: 0.75}
%%{init: {'theme': 'base', 'themeVariables': {
  'primaryColor': '#3d2f14',
  'primaryTextColor': '#e8dcc4',
  'primaryBorderColor': '#d4af37',
  'lineColor': '#d4af37',
  'edgeLabelBackground': '#0d1b2a',
  'fontFamily': 'Georgia, serif'
}}}%%
flowchart LR
    subgraph repo["one repo"]
        direction LR
        subgraph h["heroes-service"]
            hb["build.gradle.kts<br/>applies plugin"]:::config
        end
        subgraph m["monsters-service"]
            mb["build.gradle.kts<br/>applies plugin"]:::config
        end
        subgraph a["api-spec"]
            ab["build.gradle.kts<br/>applies plugin"]:::config
        end
        h --> a
        m --> a
    end
    plugin["build-logic-plugin<br/>(published)"]:::plugin
    catalog["odyssey-catalog<br/>(published)"]:::catalog
    plugin -.-> hb
    plugin -.-> mb
    plugin -.-> ab
    catalog -.->|versions| hb
    catalog -.->|versions| mb
    catalog -.->|versions| ab
    catalog -.->|versions| plugin
    style repo fill:#0d1b2a,stroke:#d4af37,stroke-width:2px,stroke-dasharray:4 4
    style h fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style m fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style a fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    classDef config fill:#7a1f2b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
    classDef plugin fill:#1b3a4b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
    classDef catalog fill:#1b3a4b,stroke:#a8c5d4,stroke-width:2px,color:#e8dcc4
```

</div>

---
transition: slide-down
---

<StageFooter icon="laurel" name="Ithaca, Regained" />

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

<StageFooter icon="laurel" name="Ithaca, Regained" />

## Pros & Cons

<br/>

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold)">Pros</h3>
    <ul>
      <li>Build logic published once, consumed by any project via <code>plugins { id(...) }</code></li>
      <li>True reuse — no copy-pasting <code>buildSrc</code> or the catalog into new repos</li>
      <li>Central place to version and release build-logic changes</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-wine)">Cons</h3>
    <ul>
      <li>Adds publishing and versioning ceremony for the plugin itself</li>
      <li>Requires a real (or local) plugin repository and release discipline</li>
      <li>Shared build-logic changes now need their own release before consumers pick them up</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

<StageFooter icon="laurel" name="Ithaca, Regained" />

## Conclusion

<div class="absolute inset-0 flex items-center justify-center text-center text-xl px-20">

Home again, transformed — build logic any project can adopt, with no copying, no drift.

</div>

<!--
Home again, transformed: what started as a single module is now build logic any project
can adopt, published and versioned like any other dependency — no copying, no drift, no cave.
The voyage that began with one simple build ends with one that scales to as many as you need.
-->

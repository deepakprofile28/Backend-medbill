package com.medbill.service.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClinicalKnowledgeBase {

    private static final List<ClinicalDrugProfile> PROFILES = new ArrayList<>();

    static {
        // =========================================================================
        // 1. ANALGESICS, NSAIDs & ANTIPYRETICS (Fever, Body Pain, Headache, Arthritis)
        // =========================================================================
        add("Dolo 650 / Paracetamol 650mg",
            "Paracetamol / Acetaminophen (650mg)",
            "Analgesic & Antipyretic",
            "Adults: 1 tablet every 6 to 8 hours after food as needed for fever or mild-to-moderate pain. Maximum 4000mg/day. Keep at least 4-6 hours between doses.",
            "Rare: Nausea, epigastric upset, skin allergy. CAUTION: High doses cause severe liver toxicity. Do not combine with other paracetamol medicines or alcohol.",
            "1. Calpol 650 (GSK)\n2. Crocin 650 (Haleon)\n3. P-650 (Apex Labs)\n4. Pacimol 650 (Ipca)",
            "First-line antipyretic for dengue, viral fever, and post-vaccination fever. Safe in pregnancy under medical advice.",
            "dolo", "dolo 650", "paracetamol", "fever", "crocin", "calpol", "headache", "body pain", "temperature");

        add("Combiflam / Ibuprofen + Paracetamol",
            "Ibuprofen (400mg) + Paracetamol (325mg)",
            "Non-Steroidal Anti-Inflammatory Drug (NSAID)",
            "1 tablet twice or thrice daily strictly AFTER meals. Drink with full glass of water. Do not take on empty stomach.",
            "Gastric acidity, heartburn, nausea, gastric ulceration. Contraindicated in active peptic ulcer, severe renal impairment, and asthma triad.",
            "1. Brufen Plus (Abbott)\n2. Ibugesic Plus (Cipla)\n3. Flexon (Aristo)\n4. Zupar (Glenmark)",
            "Dual-action analgesic for toothache, joint pain, muscular sprain, and menstrual cramps. Co-prescribe antacid (Pan 40) if patient has acidity.",
            "combiflam", "brufen", "ibuprofen", "toothache", "dental pain", "body ache", "swelling", "sprain", "muscle pain");

        add("Zerodol-SP / Aceclofenac + Paracetamol + Serratiopeptidase",
            "Aceclofenac (100mg) + Paracetamol (325mg) + Serratiopeptidase (15mg)",
            "Anti-Inflammatory, Analgesic & Anti-edema",
            "1 tablet twice daily after meals for 3 to 5 days. Do not crush or chew.",
            "Mild dizziness, gastric irritation, nausea. Contraindicated in bleeding disorders, active ulcers, or hepatic failure.",
            "1. Hifenac-D / SP (Intas)\n2. Signoflam (Lupin)\n3. Aceclo-SP (Aristo)\n4. Aldigesic-SP (Alkem)",
            "Triple-action formulation for severe post-operative trauma, orthopaedic bone fracture pain, dental extraction, and inflammatory swelling.",
            "zerodol", "zerodol-sp", "zerodol p", "aceclofenac", "serratiopeptidase", "injury", "fracture", "swelling", "ortho");

        add("Voveran 50 / Diclofenac Sodium 50mg",
            "Diclofenac Sodium (50mg)",
            "Potent NSAID Analgesic",
            "1 tablet 2 to 3 times daily post-meals. Elderly patients should take the lowest effective dose.",
            "Gastric erosion, fluid retention, elevation in liver enzymes. Contraindicated in severe congestive heart failure and active GI bleed.",
            "1. Dynapar (Troikaa)\n2. Jonac (Zydus)\n3. Diclogesic (Torrent)\n4. Nac (Sun Pharma)",
            "Potent anti-arthritic analgesic for rheumatoid arthritis, osteoarthritis, acute gout attack, and kidney stone renal colic pain.",
            "voveran", "diclofenac", "dynapar", "joint pain", "arthritis", "gout", "back pain", "sciatica", "renal colic");

        add("Meftal-Spas / Dicyclomine + Mefenamic Acid",
            "Mefenamic Acid (250mg) + Dicyclomine Hydrochloride (10mg)",
            "Antispasmodic & Analgesic",
            "1 tablet as needed for spasmodic abdominal colic or dysmenorrhea, maximum 3 times daily after food.",
            "Dry mouth, blurred vision, mild drowsiness, nausea. Caution in glaucoma and urinary retention.",
            "1. Spasmo-Proxyvon (Wockhardt)\n2. Colimex (Wallace)\n3. Cyclopam (Indoco)\n4. Spasmonil (Cipla)",
            "Specific first-line remedy for acute stomach cramps, intestinal colic, and menstrual pain (period pain).",
            "meftal", "meftal spas", "period pain", "stomach pain", "cramp", "menstrual", "colic", "dicyclomine");

        add("Ultracet / Tramadol + Paracetamol",
            "Tramadol Hydrochloride (37.5mg) + Paracetamol (325mg)",
            "Opioid Analgesic Combination",
            "1 to 2 tablets every 6 hours as needed for severe acute pain. Maximum 8 tablets daily. Doctor prescription strictly mandatory.",
            "Drowsiness, constipation, dizziness, nausea, physical dependence with prolonged use.",
            "1. Calpol-T (GSK)\n2. Urgendol-P (Sun)\n3. Tramazac-P (Zydus)\n4. Dolzero (Alkem)",
            "Reserved for moderate to severe acute pain, post-surgical recovery, and refractory musculoskeletal pain unresponsive to NSAIDs.",
            "ultracet", "tramadol", "severe pain", "surgery pain", "bone pain", "cancer pain");

        add("Ecosprin 75 / Aspirin 75mg Gastro-resistant",
            "Acetylsalicylic Acid / Aspirin (75mg)",
            "Antiplatelet & Cardioprotective",
            "1 tablet once daily with water after meals, preferably at the same time each day. Long-term compliance essential.",
            "Mild dyspepsia, increased bleeding tendency, easy bruising. Contraindicated in hemophilia, active ulcers, or aspirin-induced asthma.",
            "1. Delisprin 75 (Aristo)\n2. Sprin 75 (Alkem)\n3. Loprin 75 (Unichem)\n4. ASA 75 (Zydus)",
            "Cardiovascular prophylaxis to prevent myocardial infarction (heart attack), ischemic stroke, and transient ischemic attacks.",
            "ecosprin", "aspirin", "ecosprin 75", "ecosprin 150", "blood thinner", "heart attack", "stroke prevention", "cardiac");

        // =========================================================================
        // 2. ANTIBIOTICS, ANTIFUNGALS & ANTIVIRALS
        // =========================================================================
        add("Augmentin 625 Duo",
            "Amoxicillin (500mg) + Potassium Clavulanate (125mg)",
            "Beta-lactam Antibiotic + Beta-lactamase Inhibitor",
            "1 tablet twice daily (every 12 hours) immediately at the start of a meal for 5 to 7 days.",
            "Diarrhea, nausea, loose stools, fungal rash. CONTRAINDICATED in confirmed penicillin allergy.",
            "1. Clavam 625 (Alkem)\n2. Moxikind-CV 625 (Mankind)\n3. Sensiclav 625 (Macleods)\n4. Novamox-CV (Cipla)",
            "Broad spectrum gold standard antibiotic for severe bronchitis, sinusitis, dental abscess, cellulitis, and post-surgery prophylaxis.",
            "augmentin", "augmentin 625", "amoxicillin", "clavam", "moxikind", "dental infection", "pus", "sinus", "antibiotic");

        add("Azithral 500 / Azithromycin 500mg",
            "Azithromycin Dihydrate (500mg)",
            "Macrolide Antibiotic",
            "1 tablet once daily 1 hour before or 2 hours after meals for 3 to 5 days. Take at the exact same hour every day.",
            "Abdominal cramps, nausea, vomiting, loose stools. Caution in hepatic impairment and cardiac arrhythmias.",
            "1. Azee 500 (Cipla)\n2. Zady 500 (Mankind)\n3. Azax 500 (Sun Pharma)\n4. ATM 500 (Indoco)",
            "High tissue penetration antibiotic for tonsillitis, pharyngitis, acute bacterial sinusitis, pneumonia, and typhoid fever.",
            "azithral", "azithromycin", "azee", "throat infection", "tonsil", "cough infection", "chest cold", "chlamydia");

        add("Taxim-O 200 / Cefixime 200mg",
            "Cefixime Trihydrate (200mg)",
            "Third-Generation Cephalosporin Antibiotic",
            "1 tablet twice daily (every 12 hours) after food for 5 to 10 days.",
            "Diarrhea, loose motions, abdominal bloating, headache. Caution in penicillin cross-sensitivity.",
            "1. Zifi 200 (FDC)\n2. Mahacef 200 (Mankind)\n3. Cefolac 200 (Macleods)\n4. Omnicef 200 (Aristo)",
            "Potent oral cephalosporin indicated for Urinary Tract Infections (UTI), gonococcal urethritis, typhoid (enteric) fever, and otitis media.",
            "taxim", "taxim-o", "cefixime", "zifi", "mahacef", "uti", "urine infection", "typhoid", "ear infection");

        add("Ciplox 500 / Ciprofloxacin 500mg",
            "Ciprofloxacin Hydrochloride (500mg)",
            "Fluoroquinolone Antibiotic",
            "1 tablet twice daily (every 12 hours) with copious fluids. Avoid milk, antacids, or iron supplements within 2 hours of ingestion.",
            "Nausea, tendon tenderness/tendinitis, insomnia, phototoxicity. Contraindicated in children and pregnancy (cartilage toxicity).",
            "1. Cifran 500 (Sun Pharma)\n2. Ciprobid 500 (Zydus)\n3. Alcipro 500 (Alkem)\n4. Quintor 500 (Torrent)",
            "First line for complicated bacterial diarrhea, pelvic inflammatory disease, bone and joint infections, and prostatitis.",
            "ciplox", "ciprofloxacin", "cifran", "loose stool", "dysentery", "kidney infection", "prostatitis");

        add("Oflox-OZ / Ofloxacin + Ornidazole",
            "Ofloxacin (200mg) + Ornidazole (500mg)",
            "Antidiarrheal & Antiprotozoal Antibacterial",
            "1 tablet twice daily after meals for 3 to 5 days. Ensure complete rehydration therapy with ORS.",
            "Metallic taste in mouth, nausea, headache, dizziness, dark urine. Avoid alcohol consumption completely.",
            "1. Zenflox-OZ (Mankind)\n2. O2 Tablet (Medley)\n3. Ornof (Aristo)\n4. Oflotas-OZ (Intas)",
            "Standard emergency cure for acute bacterial diarrhea, amoebiasis, giardiasis, food poisoning, and gastrointestinal infections.",
            "oflox", "oflox oz", "zenflox", "o2", "food poisoning", "diarrhea", "loose motion", "watery stool", "amoebic");

        add("Doxy-1 / Doxycycline 100mg",
            "Doxycycline Hyclate (100mg)",
            "Tetracycline-class Antibiotic",
            "1 capsule twice on day 1, followed by 1 capsule once daily. Must be swallowed upright with a full glass of water to avoid esophageal erosion.",
            "Severe photosensitivity (sunburn), esophageal burning, nausea. Contraindicated in pregnancy and children under 8 years (teeth discoloration).",
            "1. Doxicip 100 (Cipla)\n2. Monodox (Sun)\n3. Biodoxi (Biochem)\n4. Microdox (Micro Labs)",
            "Broad spectrum treatment for acne vulgaris, scrub typhus, malaria prophylaxis, chlamydia, Lyme disease, and atypical pneumonia.",
            "doxy", "doxycycline", "doxy 1", "acne", "pimples", "tick bite", "typhus", "malaria prophylaxis");

        add("Flagyl 400 / Metronidazole 400mg",
            "Metronidazole (400mg)",
            "Nitroimidazole Antiprotozoal & Anaerobic Antibacterial",
            "1 tablet thrice daily after food for 5 to 7 days.",
            "Prominent metallic taste, anorexia, nausea, disulfiram-like reaction with alcohol (severe vomiting/tachycardia).",
            "1. Metrogyl 400 (J.B. Chemicals)\n2. Aristogyl (Aristo)\n3. Aldezole (Albert David)\n4. Metro (Cipla)",
            "Essential for anaerobic dental infections, amoebic dysentery, bacterial vaginosis, and post-appendectomy abdominal sepsis.",
            "flagyl", "metrogyl", "metronidazole", "dental pus", "anaerobic", "vaginal discharge", "bacterial vaginosis");

        add("Forcan 150 / Fluconazole 150mg",
            "Fluconazole (150mg)",
            "Triazole Antifungal",
            "1 tablet as a single oral dose for vaginal candidiasis. For tinea fungal skin infection: 1 tablet weekly for 4 weeks.",
            "Mild nausea, headache, transient elevation of liver transaminases.",
            "1. Flucos 150 (Cipla)\n2. Syscan 150 (Torrent)\n3. Zocon 150 (FDC)\n4. Afen 150 (Alkem)",
            "Systemic antifungal of choice for candida yeast infections, oral thrush, and ringworm tinea cruris/corporis.",
            "forcan", "fluconazole", "flucos", "zocon", "fungal", "yeast infection", "vaginal itching", "thrush", "ringworm");

        add("Candiforce 200 / Itraconazole 200mg",
            "Itraconazole Pellets (200mg)",
            "Broad-spectrum Antifungal",
            "1 capsule once or twice daily immediately after a full meal (fatty meal aids absorption) for 7 to 14 days.",
            "Mild indigestion, headache, elevated LFT. Contraindicated in congestive heart failure and pregnancy.",
            "1. Itrasys 200 (Systopic)\n2. Canditral 200 (Glenmark)\n3. Sporanox (Janssen)\n4. Itraone (Cipla)",
            "First line for stubborn ringworm (Tinea corporis), fungal nail infection (onychomycosis), and systemic mycoses.",
            "candiforce", "itraconazole", "itrasys", "stubborn fungus", "nail fungus", "tinea", "skin itching");

        add("Zovirax 400 / Acyclovir 400mg",
            "Acyclovir (400mg)",
            "Antiviral Agent",
            "1 tablet 5 times daily (every 4 hours while awake) for 7 to 10 days. Maintain high fluid intake.",
            "Nausea, headache, dizziness. Ensure renal hydration to prevent renal tubular precipitation.",
            "1. Herperax 400 (Micro Labs)\n2. Ocuvir 400 (FDC)\n3. Acivir 400 (Cipla)\n4. Lovir (Torrent)",
            "Antiviral therapy for Herpes Zoster (Shingles), Herpes Simplex cold sores, and Varicella chickenpox outbreak.",
            "zovirax", "acyclovir", "herperax", "ocuvir", "shingles", "herpes", "cold sore", "chickenpox", "blisters");

        // =========================================================================
        // 3. GASTROENTEROLOGY & ACID PEPTIC DISEASE
        // =========================================================================
        add("Pan 40 / Pantoprazole 40mg",
            "Pantoprazole Sodium Gastro-resistant (40mg)",
            "Proton Pump Inhibitor (PPI / Antacid)",
            "1 tablet once daily in the morning, strictly 30 to 60 minutes BEFORE breakfast with water.",
            "Headache, loose stools, abdominal gas. Prolonged usage (>1 year) may deplete Vitamin B12 and Magnesium.",
            "1. Pantocid 40 (Sun Pharma)\n2. Pantodac 40 (Zydus)\n3. Nupenta 40 (Macleods)\n4. Protium 40 (Alkem)",
            "Superior acid suppression for GERD heartburn, erosive esophagitis, gastric ulcers, and prevention of NSAID-induced gastritis.",
            "pan 40", "pantoprazole", "pantocid", "pantodac", "acidity", "gas", "gerd", "heartburn", "ulcer", "stomach burn");

        add("Pan-D / Pantoprazole + Domperidone SR",
            "Pantoprazole (40mg) + Domperidone Sustained Release (30mg)",
            "PPI + Prokinetic Antiemetic",
            "1 capsule once daily early morning 30 minutes before breakfast.",
            "Dry mouth, mild dizziness, abdominal cramps. Avoid in patients with cardiac QT prolongation.",
            "1. Pantocid-DSR (Sun Pharma)\n2. Dompan-SR (Medley)\n3. Pantocar-D (Micro Labs)\n4. Pepcia-D (FDC)",
            "Highly effective for acid reflux accompanied by bloating, nausea, regurgitation, belching, and delayed stomach emptying.",
            "pan d", "pan-d", "pantocid dsr", "pantoprazole domperidone", "vomiting with acidity", "bloating", "burping", "reflux");

        add("Omez 20 / Omeprazole 20mg",
            "Omeprazole (20mg)",
            "Proton Pump Inhibitor",
            "1 capsule once daily 30 minutes before breakfast with a glass of water.",
            "Mild diarrhea, abdominal pain, flatulence, nausea.",
            "1. Ocid 20 (Zydus)\n2. Lokit 20 (Torrent)\n3. Omecip (Cipla)\n4. Lomac (Macleods)",
            "Standard PPI for acute peptic ulcer disease, Zollinger-Ellison syndrome, and H. pylori eradication therapy.",
            "omez", "omeprazole", "ocid", "peptic ulcer", "acid secretion", "sour stomach");

        add("Razo 20 / Rabeprazole Sodium 20mg",
            "Rabeprazole Sodium (20mg)",
            "Fast-acting PPI",
            "1 tablet once daily morning before meals. Rapid onset of acid suppression within 1 hour.",
            "Infection vulnerability with long term use, mild headache, asthenia.",
            "1. Veloz 20 (Torrent)\n2. Happi 20 (Zydus)\n3. Rabicip 20 (Cipla)\n4. Rablet 20 (Lupin)",
            "Fastest-acting proton pump inhibitor providing rapid symptom relief in acute acid reflux and nocturnal heartburn.",
            "razo", "rabeprazole", "veloz", "happi", "fast acidity relief", "night heartburn");

        add("Rantac 150 / Ranitidine 150mg",
            "Ranitidine Hydrochloride (150mg)",
            "H2-Receptor Antagonist",
            "1 tablet twice daily (morning & night) 30 minutes before food.",
            "Rare: Headache, tiredness, transient constipation.",
            "1. Aciloc 150 (Cadila)\n2. Zinetac 150 (GSK)\n3. Histac 150 (Ranbaxy)\n4. Ranitin (Micro)",
            "Mild, gentle acid suppression for temporary indigestion, gastritis, and night-time sour belching.",
            "rantac", "aciloc", "ranitidine", "h2 blocker", "mild acidity", "indigestion", "sour burp");

        add("Emeset 4 / Ondansetron 4mg",
            "Ondansetron Hydrochloride (4mg)",
            "5-HT3 Receptor Antagonist Antiemetic",
            "1 tablet or mouth-dissolving strip 30 minutes before meals or travel. Max 8mg twice daily.",
            "Mild headache, constipation, sensation of flushing or warmth.",
            "1. Vomikind 4 (Mankind)\n2. Ondem 4 (Alkem)\n3. Zofer 4 (Sun Pharma)\n4. Periset (Ipca)",
            "Gold standard anti-vomiting medicine for motion sickness, gastroenteritis vomiting, and post-chemotherapy nausea.",
            "emeset", "vomikind", "ondansetron", "ondem", "vomiting", "nausea", "motion sickness", "puke");

        add("Sucrafil Suspension / Sucralfate + Oxetacaine",
            "Sucralfate (1000mg) + Oxetacaine (20mg) per 10ml",
            "Mucosal Protective & Local Anesthetic",
            "10ml (2 teaspoons) 1 hour before meals and at bedtime on an empty stomach. Shake well before use.",
            "Constipation, dry mouth, mild nausea. Avoid taking antacids within 30 minutes of this suspension.",
            "1. Sucrafil-O (Fourrts)\n2. Urset (Torrent)\n3. Macralfate-O (Macleods)\n4. Pepsigard (Dr. Reddy's)",
            "Forms a physical protective mucosal shield over stomach ulcers and relieves burning pain immediately via local anesthetic Oxetacaine.",
            "sucrafil", "sucralfate", "oxetacaine", "ulcer coating", "stomach burning", "gastric ulcer pain", "mucosal ulcer");

        add("Lopamide / Loperamide 2mg",
            "Loperamide Hydrochloride (2mg)",
            "Antidiarrheal Motility Inhibitor",
            "Initial dose 2 capsules (4mg), followed by 1 capsule (2mg) after each unformed loose stool. Max 16mg/day. Do not use in bacterial bloody dysentery.",
            "Abdominal cramps, drowsiness, dry mouth, constipation if overused.",
            "1. Imodium 2mg (Johnson & Johnson)\n2. Eldoper (Micro Labs)\n3. Loparex (Torrent)\n4. Roko (Cipla)",
            "Slowing intestinal peristalsis for rapid control of non-infectious acute travelers' diarrhea and irritable bowel syndrome.",
            "lopamide", "loperamide", "imodium", "eldoper", "stop loose motion", "travel diarrhea", "frequent stool");

        add("Electral Powder / WHO Oral Rehydration Salts (ORS)",
            "Sodium Chloride (2.6g) + Potassium Chloride (1.5g) + Sodium Citrate (2.9g) + Dextrose (13.5g)",
            "Oral Electrolyte Replenisher",
            "Dissolve entire packet in exactly 1 Liter of clean drinking water. Drink small sips throughout the day as tolerated. Discard after 24 hours.",
            "None when properly diluted. Do not mix with milk, soup, or fruit juice.",
            "1. Walyte ORS (Wallace)\n2. Reliyt (Cipla)\n3. Prolyte ORS (Alkem)\n4. Enerzal (FDC)",
            "WHO-recommended life-saving oral rehydration for dehydration caused by diarrhea, vomiting, heat stroke, and heavy sweating.",
            "electral", "ors", "rehydration", "electrolyte", "dehydration", "weakness diarrhea", "walyte");

        add("Duphalac Syrup / Lactulose Solution",
            "Lactulose (10g / 15ml)",
            "Osmotic Laxative",
            "15ml to 30ml once daily at bedtime with plenty of water. Can take 24 to 48 hours for full laxative action.",
            "Abdominal flatulence, mild belching, abdominal cramps, diarrhea with overdose.",
            "1. Cremaffin Plus (Abbott)\n2. Looz (Intas)\n3. Lactihep (Sun)\n4. Cadilose (Cadila)",
            "Gentle, non-habit forming stool softener for chronic constipation, elderly constipation, and hepatic encephalopathy.",
            "duphalac", "lactulose", "cremaffin", "looz", "constipation", "hard stool", "bowel movement", "laxative");

        // =========================================================================
        // 4. RESPIRATORY, COUGH, ALLERGY & ASTHMA
        // =========================================================================
        add("Cetzine 10mg / Cetirizine 10mg",
            "Cetirizine Hydrochloride (10mg)",
            "Second-Generation Antihistamine",
            "1 tablet once daily at bedtime. Children (6-12 yrs): 5mg twice daily or 10mg at night.",
            "Mild drowsiness, dry mouth, fatigue, headache. Caution when driving or operating machinery.",
            "1. Alerid 10 (Cipla)\n2. Okacet 10 (Cipla)\n3. Zyrtec 10 (Dr. Reddy's)\n4. Incid-L (Bayer)",
            "Relief from allergic rhinitis, sneezing, watery eyes, runny nose, itchy throat, insect bites, and urticaria hives.",
            "cetzine", "cetirizine", "alerid", "okacet", "allergy", "sneezing", "cold", "running nose", "itching", "hives");

        add("Montair-LC / Montelukast + Levocetirizine",
            "Montelukast Sodium (10mg) + Levocetirizine Dihydrochloride (5mg)",
            "Leukotriene Receptor Antagonist + Antihistamine",
            "1 tablet once daily in the evening / bedtime with water for 10 to 14 days.",
            "Mild drowsiness, dry mouth, headache, vivid dreams/insomnia.",
            "1. Telekast-L (Lupin)\n2. Montek-LC (Sun Pharma)\n3. Levocet-M (Cipla)\n4. Romilast-L (Ranbaxy)",
            "Dual-mechanism treatment for allergic rhinitis associated with bronchial asthma, nighttime allergic cough, and chronic allergic sinusitis.",
            "montair", "montair lc", "telekast", "montek", "levocetirizine montelukast", "allergic cough", "night cough", "asthma allergy");

        add("Allegra 120 / Fexofenadine 120mg",
            "Fexofenadine Hydrochloride (120mg)",
            "Non-Sedating Third Generation Antihistamine",
            "1 tablet once daily with water. Avoid drinking fruit juices (grapefruit, apple, orange) within 4 hours as they reduce bioavailability.",
            "Virtually zero sedation/drowsiness. Rare headache, nausea.",
            "1. Fexova 120 (Ipca)\n2. Histafree 120 (Mankind)\n3. Fexy 120 (Cadila)\n4. Fastway (Sun)",
            "100% Non-drowsy antihistamine for day workers, pilots, drivers suffering from pollen allergy, hay fever, and chronic idiopathic urticaria.",
            "allegra", "allegra 120", "fexofenadine", "non drowsy allergy", "pollen allergy", "hay fever", "dust allergy");

        add("Sinarest Tablet / Cold & Sinus Relief",
            "Paracetamol (500mg) + Phenylephrine (10mg) + Chlorpheniramine Maleate (2mg)",
            "Comprehensive Cold & Sinus Decongestant",
            "1 tablet every 6 to 8 hours after food. Max 4 tablets per day.",
            "Drowsiness, dry mouth, mild elevation in blood pressure, dizziness.",
            "1. D-Cold Total (Paras)\n2. Cheston Cold (Cipla)\n3. Febrex Plus (Indoco)\n4. Solvin Cold (Ipca)",
            "All-in-one remedy for blocked nose, sinus headache, watery eyes, feverishness, and severe common cold.",
            "sinarest", "d cold", "cheston cold", "sinus cold", "blocked nose", "nasal congestion", "heavy head", "cold fever");

        add("Ascoril-LS Syrup / Ambroxol + Levosalbutamol + Guaiphenesin",
            "Ambroxol (30mg) + Levosalbutamol (1mg) + Guaiphenesin (50mg) per 5ml",
            "Mucolytic Expectorant & Bronchodilator",
            "Adults: 10ml thrice daily after food. Children (6-12 yrs): 5ml thrice daily with warm water.",
            "Mild hand tremors, rapid heartbeat (palpitations), dizziness, gastric upset.",
            "1. Grilinctus-LS (Panacea)\n2. Bro-Zedex LS (Wockhardt)\n3. Macbery-LS (Macleods)\n4. Asthalin Expectorant (Cipla)",
            "Clears chest mucus in productive wet cough, acute bronchitis, smoker's cough, and asthma-associated mucus plugs.",
            "ascoril", "ascoril ls", "grilinctus", "wet cough", "phlegm", "cough syrup", "chest mucus", "bronchitis");

        add("Ascoril-D Syrup / Dextromethorphan + Chlorpheniramine + Phenylephrine",
            "Dextromethorphan HBr (10mg) + CPM (2mg) + Phenylephrine (5mg) per 5ml",
            "Antitussive Cough Suppressant",
            "Adults: 10ml thrice daily after food. Shake well before pouring.",
            "Drowsiness, slight lightheadedness, dry throat, constipation.",
            "1. Benadryl DR (J&J)\n2. Chericof (Sun)\n3. Zedex (Wockhardt)\n4. Phensedyl-DX (Abbott)",
            "Suppresses persistent dry tickling cough, throat irritation, and nighttime coughing spasms without phlegm.",
            "ascoril d", "benadryl", "dry cough", "tickling throat", "cough suppressant", "hacking cough");

        add("Asthalin Inhaler / Salbutamol 100mcg",
            "Salbutamol / Albuterol (100mcg per puff)",
            "Short-Acting Beta-2 Agonist (SABA) Reliever",
            "1 to 2 puffs inhaled immediately during sudden acute shortness of breath or asthma attack. Rinse mouth with water after inhalation.",
            "Transient finger tremors, palpitations, tachycardia, throat irritation.",
            "1. Derihaler (Cadila)\n2. Ventorlin (GSK)\n3. Salamol (Teva)\n4. Butovent (Alkem)",
            "Life-saving fast-acting rescue bronchodilator for acute asthma attack, bronchospasm, and wheezing episodes.",
            "asthalin", "salbutamol", "inhaler", "asthma attack", "wheezing", "breathlessness", "pump", "puff");

        add("Budecort 200 Inhaler / Budesonide",
            "Budesonide (200mcg per puff)",
            "Inhaled Corticosteroid (ICS) Controller",
            "1 to 2 puffs twice daily (morning & night). MUST rinse mouth thoroughly with water and spit out after use to prevent oral fungal thrush.",
            "Hoarseness of voice, oral candidiasis (fungus on tongue), throat irritation.",
            "1. Pulmicort (AstraZeneca)\n2. Budenase (Cipla)\n3. Nebicard (Lupin)\n4. Foracort (Cipla)",
            "Maintenance controller therapy to suppress underlying bronchial inflammation in chronic asthma and COPD.",
            "budecort", "budesonide", "foracort", "preventer inhaler", "chronic asthma", "copd", "bronchial swelling");

        // =========================================================================
        // 5. DIABETES MANAGEMENT
        // =========================================================================
        add("Glycomet 500 / Metformin 500mg",
            "Metformin Hydrochloride (500mg)",
            "Biguanide Oral Antidiabetic Agent",
            "1 tablet once or twice daily strictly WITH or immediately after meals to minimize stomach upset. Long term therapy.",
            "Nausea, metallic taste, diarrhea, flatulence. Lactic acidosis in rare cases. Avoid in severe renal failure (eGFR < 30).",
            "1. Glucophage 500 (Merck)\n2. Cetapin 500 (Sanofi)\n3. Obimet 500 (Abbott)\n4. Formin (Alkem)",
            "Cornerstone first-line treatment for Type 2 Diabetes Mellitus. Reduces insulin resistance, suppresses hepatic glucose production, and aids weight management.",
            "glycomet", "metformin", "sugar", "diabetes", "glucophage", "blood sugar", "fasting glucose", "type 2 diabetes");

        add("Amaryl 1mg / 2mg / Glimepiride",
            "Glimepiride (1mg / 2mg)",
            "Second Generation Sulfonylurea",
            "1 tablet once daily immediately before or with the first main meal / breakfast of the day.",
            "HYPOGLYCEMIA (low blood sugar: sweating, shakiness, dizziness, hunger), mild weight gain. Keep sweets/sugar packets handy.",
            "1. Glimisave 1/2 (Eris)\n2. Glimestar (Mankind)\n3. Zoryl (Intas)\n4. Euglim (Torrent)",
            "Stimulates pancreatic beta-cells to secrete insulin. Highly potent in reducing post-prandial hyperglycemia.",
            "amaryl", "glimepiride", "glimisave", "low sugar", "insulin secretion", "diabetes tablet");

        add("Glycomet-GP 1 / Glimepiride + Metformin",
            "Glimepiride (1mg) + Metformin Hydrochloride Prolonged Release (500mg)",
            "Dual Mechanism Antidiabetic Combination",
            "1 tablet once daily with breakfast. Monitor fasting and post-meal blood sugar levels routinely.",
            "Risk of hypoglycemia, abdominal fullness, nausea, loose stools.",
            "1. Glimisave-M1 (Eris)\n2. Glimestar-M1 (Mankind)\n3. Zoryl-M1 (Intas)\n4. Triglynase (Sun)",
            "Synergistic dual therapy targeting both insulin deficiency and insulin resistance when Metformin monotherapy is insufficient.",
            "glycomet gp", "glimepiride metformin", "combination sugar tablet", "sugar control", "high hba1c");

        add("Forxiga 10mg / Dapagliflozin 10mg",
            "Dapagliflozin (10mg)",
            "SGLT2 Inhibitor",
            "1 tablet once daily in the morning with or without food. Drink abundant water throughout the day.",
            "Increased urinary frequency, urinary tract infections, genital fungal infections, dehydration.",
            "1. Oxra 10 (Sun Pharma)\n2. Dapa-10 (Cipla)\n3. Dapavel 10 (Lupin)\n4. Justo (Intas)",
            "Modern SGLT2 inhibitor that eliminates excess blood sugar via urine. Proven cardiovascular and kidney-protective benefits in diabetic and heart failure patients.",
            "forxiga", "dapagliflozin", "oxra", "sglt2", "heart failure diabetes", "sugar urine", "kidney protection");

        add("Januvia 100mg / Sitagliptin 100mg",
            "Sitagliptin Phosphate (100mg)",
            "DPP-4 Inhibitor (Gliptin)",
            "1 tablet once daily with or without food.",
            "Virtually zero risk of hypoglycemia, weight neutral. Rare upper respiratory infection, headache.",
            "1. Istavel 100 (Sun Pharma)\n2. Zita 100 (Glenmark)\n3. Janumet (Sitagliptin+Metformin)\n4. Sitaglyn (Mankind)",
            "Glucose-dependent insulin release with high cardiovascular safety and zero hypoglycemia risk.",
            "januvia", "sitagliptin", "istavel", "gliptin", "weight neutral sugar tablet", "dpp4");

        // =========================================================================
        // 6. CARDIOLOGY, HYPERTENSION (BP) & CHOLESTEROL
        // =========================================================================
        add("Telma 40 / Telmisartan 40mg",
            "Telmisartan (40mg)",
            "Angiotensin II Receptor Blocker (ARB)",
            "1 tablet once daily in the morning or evening at the exact same hour every day. Lifelong therapy.",
            "Dizziness on standing quickly, hyperkalemia (high potassium). Avoid during pregnancy (teratogenic).",
            "1. Telvas 40 (Aristo)\n2. Tazloc 40 (USV)\n3. Telmikind 40 (Mankind)\n4. Telsartan (Dr. Reddy's)",
            "Gold standard frontline antihypertensive with 24-hour long-lasting BP control and organ protection for heart and kidneys.",
            "telma", "telma 40", "telmisartan", "bp", "blood pressure", "high bp", "hypertension", "telvas", "tazloc");

        add("Telma-AM / Telmisartan + Amlodipine",
            "Telmisartan (40mg) + Amlodipine Besylate (5mg)",
            "ARB + Calcium Channel Blocker Combination",
            "1 tablet once daily, preferably in the morning.",
            "Mild peripheral ankle swelling (pedal edema), dizziness, headache, flushing.",
            "1. Telvas-AM (Aristo)\n2. Tazloc-AM (USV)\n3. Amlokind-T (Mankind)\n4. Twynsta (Boehringer)",
            "Dual-action synergistic control for uncontrolled high blood pressure unresponsive to single-drug therapy.",
            "telma am", "telmisartan amlodipine", "severe bp", "double bp tablet", "ankle swelling");

        add("Cilacar 10 / Cilnidipine 10mg",
            "Cilnidipine (10mg)",
            "Dual L- and N-type Calcium Channel Blocker",
            "1 tablet once daily with water.",
            "Unlike Amlodipine, does NOT cause pedal ankle swelling. Mild headache, facial flushing.",
            "1. Cilaheart 10 (Mankind)\n2. Nexopride (Sun)\n3. Cilny (Torrent)\n4. Dilnip (Cadila)",
            "Preferred calcium channel blocker for diabetic and kidney patients with hypertension because of low ankle swelling risk and renal vasodilatation.",
            "cilacar", "cilnidipine", "cilaheart", "no swelling bp", "kidney bp tablet");

        add("Betaloc 50 / Metoprolol Succinate 50mg",
            "Metoprolol Succinate Extended Release (50mg)",
            "Cardioselective Beta-1 Blocker",
            "1 tablet once daily with or immediately after food. Do NOT abruptly discontinue.",
            "Bradycardia (slow heart rate), tiredness, cold extremities, bronchospasm in asthmatics.",
            "1. Metolar-XR 50 (Cipla)\n2. Seloken-XL (AstraZeneca)\n3. Revelol-XL (Ipca)\n4. Metpure-XL (Emcure)",
            "Controls rapid heart rate, palpitations, angina pectoris chest pain, and reduces mortality after heart attack.",
            "betaloc", "metoprolol", "metolar", "palpitation", "fast heartbeat", "chest pain", "angina", "pulse rate");

        add("Atorva 10 / 20 / Atorvastatin",
            "Atorvastatin Calcium (10mg / 20mg)",
            "HMG-CoA Reductase Inhibitor (Statin)",
            "1 tablet once daily at bedtime (cholesterol synthesis peaks at night). Regular lipid profile monitoring.",
            "Muscle aches/myalgia, mild elevation of liver enzymes, indigestion. Report unexplained muscle weakness promptly.",
            "1. Lipitor (Pfizer)\n2. Storvas (Sun Pharma)\n3. Tonact (Lupin)\n4. Atocor (Dr. Reddy's)",
            "Lowers LDL bad cholesterol and triglycerides, raises HDL good cholesterol, and stabilizes arterial plaque to prevent strokes and heart attacks.",
            "atorva", "atorvastatin", "cholesterol", "lipid", "triglycerides", "bad cholesterol", "fat in blood", "storvas");

        add("Rosuvas 10 / Rosuvastatin 10mg",
            "Rosuvastatin Calcium (10mg)",
            "High-Intensity Statin",
            "1 tablet once daily at night. Highest LDL reduction potency among statins.",
            "Muscle pain, abdominal discomfort, headache, mild fatigue.",
            "1. Rozavel 10 (Sun Pharma)\n2. Roseday 10 (USV)\n3. Rosuchem (Alkem)\n4. Crestor (AstraZeneca)",
            "Potent high-intensity statin for high-risk cardiac patients, severe hypercholesterolemia, and familial dyslipidemia.",
            "rosuvas", "rosuvastatin", "rozavel", "high cholesterol", "heart plaque", "crestor");

        // =========================================================================
        // 7. NEUROLOGY, CNS, PAIN & SLEEP
        // =========================================================================
        add("Clonafit 0.5 / Clonazepam 0.5mg",
            "Clonazepam (0.5mg)",
            "Benzodiazepine Anxiolytic & Anticonvulsant",
            "0.5mg once daily at night. Schedule H1 prescription strictly required. Do not stop abruptly.",
            "Drowsiness, ataxia, impaired coordination, drug dependence with long term use. Absolute ban on alcohol.",
            "1. Zapiz 0.5 (Intas)\n2. Epitril 0.5 (Novartis)\n3. Lonazep 0.5 (Sun Pharma)\n4. Rivotril (Roche)",
            "Treatment for acute panic disorder, severe generalized anxiety, and seizure/epilepsy management.",
            "clonafit", "clonazepam", "zapiz", "panic attack", "anxiety", "shivering", "fits", "seizure");

        add("Alprax 0.25 / 0.5 / Alprazolam",
            "Alprazolam (0.25mg / 0.5mg)",
            "Short-acting Anxiolytic",
            "1 tablet at night for short duration (maximum 2-4 weeks) under strict medical supervision.",
            "Daytime grogginess, memory impairment, habit-forming potential. Avoid driving.",
            "1. Restyl 0.25/0.5 (Cipla)\n2. Trika (Torrent)\n3. Zolax (Sun)\n4. Alzolam (Micro)",
            "Short-term relief of acute disabling anxiety and situational insomnia.",
            "alprax", "alprazolam", "restyl", "sleeping tablet", "anxiety sleep", "stress");

        add("Nexito 10 / Escitalopram 10mg",
            "Escitalopram Oxalate (10mg)",
            "Selective Serotonin Reuptake Inhibitor (SSRI)",
            "1 tablet once daily in the morning or evening. Therapeutic clinical response begins after 2 to 4 weeks.",
            "Nausea, insomnia, sexual dysfunction, mild tremor. Do not discontinue suddenly without tapering.",
            "1. Cilentra 10 (Torrent)\n2. Stalopam 10 (Lupin)\n3. S-Citadep (Cipla)\n4. Feliz-S (Sun)",
            "First-line antidepressant for major depressive disorder, generalized anxiety disorder, and obsessive-compulsive disorder.",
            "nexito", "escitalopram", "depression", "sadness", "mental stress", "anxiety neurosis", "ocd");

        add("Pregeb 75 / Pregabalin 75mg",
            "Pregabalin (75mg)",
            "GABA Analogue / Neuropathic Pain Agent",
            "1 capsule once daily at bedtime, may titrate to twice daily as directed by neurologist.",
            "Dizziness, somnolence, peripheral edema, weight gain, dry mouth.",
            "1. Lyrica 75 (Pfizer)\n2. Maxgalin 75 (Sun Pharma)\n3. Pregalin (Torrent)\n4. Neugaba (Zydus)",
            "Effective treatment for diabetic neuropathy tingling/burning in feet, post-herpetic neuralgia shingles nerve pain, and fibromyalgia.",
            "pregeb", "pregabalin", "lyrica", "nerve pain", "numbness", "tingling feet", "burning feet", "diabetic nerve");

        add("Neurobion Forte / Vitamin B-Complex with B12",
            "Thiamine (B1) + Riboflavin (B2) + Niacinamide (B3) + Calcium Pantothenate (B5) + Pyridoxine (B6) + Cyanocobalamin (B12)",
            "Neurotropic Vitamin Supplement",
            "1 tablet once daily after lunch or breakfast with water.",
            "Bright yellow coloration of urine (normal riboflavin excretion), mild nausea.",
            "1. Nurokind-Plus (Mankind)\n2. Becosules (Pfizer)\n3. Optineuron (Lupin)\n4. Cobadex-Forte (Sun)",
            "Repairs peripheral nerves, treats mouth ulcers, tingling in fingers/toes, general body exhaustion, and Vitamin B deficiency.",
            "neurobion", "neurobion forte", "b complex", "b12", "mouth ulcer", "weakness", "nurokind", "nerve vitamin");

        // =========================================================================
        // 8. VITAMINS, MINERALS & FIRST AID
        // =========================================================================
        add("Calcirol 60,000 IU / Vitamin D3 Sachet",
            "Cholecalciferol / Vitamin D3 (60,000 International Units)",
            "High-Potency Fat-Soluble Vitamin D3",
            "1 sachet once WEEKLY for 8 to 12 weeks. Empty granules into half glass of warm milk and drink after food. Do NOT take daily.",
            "Excessive doses lead to hypercalcemia, kidney stones, nausea. Take only as prescribed.",
            "1. D3-Must 60K (Mankind)\n2. Uprise-D3 60K (Alkem)\n3. Tayo 60K (Eris)\n4. Depura 60K (Sanofi)",
            "Treats severe Vitamin D deficiency, bone weakness (osteomalacia), muscle fatigue, and enhances calcium absorption.",
            "calcirol", "vitamin d3", "d3 sachet", "60000", "bone pain", "calcium deficiency", "fatigue", "d3");

        add("Shelcal 500 / Calcium + Vitamin D3",
            "Elemental Calcium (500mg) + Vitamin D3 (250 IU)",
            "Bone Mineral Health Supplement",
            "1 tablet once daily after lunch or dinner with water.",
            "Constipation, mild abdominal bloating. Maintain adequate fluid intake.",
            "1. Cipcal 500 (Cipla)\n2. Gemcal (Alkem)\n3. Calcimax 500 (Meyer)\n4. Osto-Poly (Apex)",
            "Maintains bone mineral density, prevents osteoporosis in menopausal women, and accelerates bone fracture healing.",
            "shelcal", "cipcal", "calcium", "shelcal 500", "osteoporosis", "weak bones", "calcium tablet");

        add("Dexorange Syrup / Iron + Folic Acid + Vitamin B12",
            "Ferric Ammonium Citrate (160mg) + Vitamin B12 (7.5mcg) + Folic Acid (0.5mg) per 15ml",
            "Hematinic Blood Builder",
            "1 tablespoon (15ml) twice daily after meals. Rinse mouth after ingestion to avoid temporary teeth staining.",
            "Dark black stools (harmless iron excretion), constipation, nausea.",
            "1. Orofer-XT (Emcure)\n2. Fefol-Z (GSK)\n3. Tonoferon (East India)\n4. Autrin (Pfizer)",
            "Rapidly boosts hemoglobin, restores red blood cells in iron-deficiency anemia, post-surgical blood loss, and pregnancy.",
            "dexorange", "iron syrup", "anemia", "low hemoglobin", "tiredness", "pale skin", "orofer");

        add("Limcee 500 / Vitamin C Chewable",
            "Ascorbic Acid / Vitamin C (500mg)",
            "Antioxidant & Immune Booster",
            "1 tablet chewed once daily after food. Delicious orange flavor.",
            "Safe, water-soluble. High doses (>2000mg) may cause mild loose stools.",
            "1. Celin 500 (GSK)\n2. Chewcee 500 (Abbott)\n3. Sukcee (Torrent)\n4. Ener-C (Cipla)",
            "Boosts immunity against viral infections, accelerates skin wound healing, scurvy treatment, and promotes collagen synthesis.",
            "limcee", "vitamin c", "celin", "chewable c", "immunity", "skin glow", "wound heal", "scurvy");

        add("Betadine 10% / Povidone Iodine Ointment",
            "Povidone Iodine (10% w/w)",
            "Broad Spectrum Topical Antiseptic Microbicide",
            "Clean wound thoroughly with saline water, apply thin layer of ointment, and cover with sterile gauze bandage. Apply 1-2 times daily.",
            "Rare localized skin irritation or itching. Avoid in thyroid disease or iodine allergy.",
            "1. Cipladine (Cipla)\n2. Wokadine (Wockhardt)\n3. Povikind (Mankind)\n4. Betasept (Alkem)",
            "Destroys bacteria, fungi, spores, and viruses in cuts, surgical wounds, burns, and infected skin abrasions.",
            "betadine", "cipladine", "povidone", "iodine", "wound ointment", "cut", "burn", "dressing", "antiseptic");

        add("T-Bact 2% Ointment / Mupirocin",
            "Mupirocin (2% w/w)",
            "Topical Antibacterial",
            "Apply thin film to affected infected area 3 times daily for up to 10 days.",
            "Mild localized burning, stinging, or redness.",
            "1. Bactroban (GSK)\n2. Mupinase (Cipla)\n3. Supirocin (Glenmark)\n4. Bupirocin (Mankind)",
            "High potency topical antibiotic against Staphylococcus and MRSA for skin boils, impetigo, infected eczema, and folliculitis.",
            "t bact", "mupirocin", "bactroban", "skin boil", "pus boil", "impetigo", "infected cut");

        add("Candid Dusting Powder / Clotrimazole 1%",
            "Clotrimazole (1% w/w)",
            "Topical Antifungal Powder",
            "Dust thoroughly on clean, dry affected areas (groin, armpits, feet) twice daily, especially after bathing.",
            "Rare skin erythema or irritation.",
            "1. Abzorb Powder (Sun Pharma)\n2. Clocip (Cipla)\n3. Canesten (Bayer)\n4. Mycoderm (FDC)",
            "Absorbs sweat, prevents fungal fungal infection, prickly heat rash, jock itch (dhobi itch), and athlete's foot.",
            "candid", "abzorb", "clotrimazole", "fungal powder", "sweat rash", "jock itch", "prickly heat", "groin itch");
    }

    private static void add(String name, String generic, String cat, String dosage, String sideEffects,
                            String subs, String assessment, String... kw) {
        PROFILES.add(new ClinicalDrugProfile(name, generic, cat, dosage, sideEffects, subs, assessment, Arrays.asList(kw)));
    }

    public static List<ClinicalDrugProfile> getAllProfiles() {
        return PROFILES;
    }

    /**
     * Smart fuzzy match across brand names, generic formulas, categories, and symptoms.
     */
    public static ClinicalDrugProfile findBestMatch(String query) {
        if (query == null || query.trim().isEmpty()) return null;
        String q = query.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").trim();

        // 1. Direct keyword match
        for (ClinicalDrugProfile p : PROFILES) {
            for (String kw : p.getKeywords()) {
                if (q.equals(kw) || q.startsWith(kw + " ") || q.endsWith(" " + kw) || q.contains(" " + kw + " ")) {
                    return p;
                }
            }
        }

        // 2. Substring containment in keyword
        for (ClinicalDrugProfile p : PROFILES) {
            for (String kw : p.getKeywords()) {
                if (q.contains(kw) || kw.contains(q)) {
                    return p;
                }
            }
        }

        // 3. Match against profile name or generic composition
        for (ClinicalDrugProfile p : PROFILES) {
            String lowerName = p.getMedicineName().toLowerCase();
            String lowerGen = p.getGenericComposition().toLowerCase();
            String[] tokens = q.split("\\s+");
            for (String t : tokens) {
                if (t.length() >= 4 && (lowerName.contains(t) || lowerGen.contains(t))) {
                    return p;
                }
            }
        }

        return null;
    }
}


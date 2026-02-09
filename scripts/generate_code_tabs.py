#!/usr/bin/env python3
"""
Script to dynamically generate code tabs in migration/examples.md
based on available code sample files.
"""
import os
from pathlib import Path

# Language mappings: directory name -> display name -> file extension
LANGUAGE_MAP = {
    "python": ("Python", "py"),
    "c#": ("C#", "cs"),
    "java": ("Java", "java"),
}

def get_available_languages(code_sample_path):
    """Get available languages for a code sample."""
    available = []
    if not os.path.exists(code_sample_path):
        return available
    
    for lang_dir in os.listdir(code_sample_path):
        lang_dir_lower = lang_dir.lower()
        if lang_dir_lower in LANGUAGE_MAP:
            display_name, ext = LANGUAGE_MAP[lang_dir_lower]
            available.append((lang_dir, display_name, ext))
    return sorted(available, key=lambda x: x[1])  # Sort by display name

def generate_tabs_section(sample_name, code_sample_path, file_name):
    """Generate tabs section for a code sample."""
    available_langs = get_available_languages(code_sample_path)
    
    if not available_langs:
        return ""
    
    tabs = []
    for lang_dir, display_name, ext in available_langs:
        file_path = f"code_samples/{sample_name}/{lang_dir}/{file_name}.{ext}"
        lang_code = "csharp" if ext == "cs" else ("java" if ext == "java" else "py")
        
        tab_content = f'''=== "{display_name}"

    ```{lang_code} title="{file_name.replace('_', ' ').title()}" linenums="1"
    --8<-- "{file_path}"
    ```'''
        tabs.append(tab_content)
    
    return "\n\n".join(tabs)

def main():
    base_path = Path(__file__).parent.parent
    docs_path = base_path / "docs"
    examples_file = docs_path / "migration" / "examples.md"
    
    # Generate tabs for authentication
    auth_path = docs_path / "code_samples" / "auth"
    auth_tabs = generate_tabs_section("auth", auth_path, "login")
    
    # Generate tabs for processing
    processing_path = docs_path / "code_samples" / "processing"
    processing_tabs = generate_tabs_section("processing", processing_path, "start_processing")
    
    # Read current file
    with open(examples_file, "r") as f:
        content = f.read()
    
    # Replace authentication section
    import re
    auth_pattern = r"## Authentication\n\n.*?(?=## Document Processing|\Z)"
    auth_replacement = f"## Authentication\n\n{auth_tabs}\n\n"
    content = re.sub(auth_pattern, auth_replacement, content, flags=re.DOTALL)
    
    # Replace processing section
    processing_pattern = r"## Document Processing\n\n.*"
    processing_replacement = f"## Document Processing\n\n{processing_tabs}"
    content = re.sub(processing_pattern, processing_replacement, content, flags=re.DOTALL)
    
    # Write back
    with open(examples_file, "w") as f:
        f.write(content)
    
    print("✅ Code tabs generated successfully!")

if __name__ == "__main__":
    main()

(function () {
    const storageKey = "unimethod.language";

    const keyedTranslations = {
        uk: {
            brand: "\u041e\u0431\u043b\u0456\u043a \u043c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u0438\u0445 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0439",
            navPublications: "\u0423\u0441\u0456 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457",
            navTemplates: "\u0428\u0430\u0431\u043b\u043e\u043d\u0438",
            navReports: "\u0417\u0432\u0456\u0442 Word",
            navAdmin: "\u0410\u0434\u043c\u0456\u043d\u0456\u0441\u0442\u0440\u0443\u0432\u0430\u043d\u043d\u044f",
            back: "\u041d\u0430\u0437\u0430\u0434",
            logout: "\u0412\u0438\u0439\u0442\u0438",
            heroText: "\u041f\u043b\u0430\u0442\u0444\u043e\u0440\u043c\u0430 \u0434\u043b\u044f \u0437\u0431\u0438\u0440\u0430\u043d\u043d\u044f, \u043f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u0438, \u0456\u043c\u043f\u043e\u0440\u0442\u0443 \u0442\u0430 \u0444\u043e\u0440\u043c\u0443\u0432\u0430\u043d\u043d\u044f \u0437\u0432\u0456\u0442\u0456\u0432 \u043f\u0440\u043e \u043c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u0456 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457 \u0432\u0438\u043a\u043b\u0430\u0434\u0430\u0447\u0456\u0432 \u0443\u043d\u0456\u0432\u0435\u0440\u0441\u0438\u0442\u0435\u0442\u0443.",
            sectionTitle: "\u041e\u0441\u043d\u043e\u0432\u043d\u0456 \u043c\u043e\u0436\u043b\u0438\u0432\u043e\u0441\u0442\u0456",
            sectionText: "\u041e\u0431\u0435\u0440\u0456\u0442\u044c \u043f\u043e\u0442\u0440\u0456\u0431\u043d\u0438\u0439 \u0440\u043e\u0437\u0434\u0456\u043b \u0434\u043b\u044f \u0440\u043e\u0431\u043e\u0442\u0438 \u0437 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044f\u043c\u0438, \u0448\u0430\u0431\u043b\u043e\u043d\u0430\u043c\u0438 \u0430\u0431\u043e \u0437\u0432\u0456\u0442\u0430\u043c\u0438.",
            addTitle: "\u0414\u043e\u0434\u0430\u0442\u0438 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044e",
            addText: "\u0420\u0443\u0447\u043d\u0435 \u0434\u043e\u0434\u0430\u0432\u0430\u043d\u043d\u044f \u043c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u043e\u0457 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457 \u0437 \u043f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u043e\u044e \u0444\u043e\u0440\u043c\u0430\u0442\u0443 \u0442\u0430 \u043c\u043e\u0436\u043b\u0438\u0432\u0438\u0445 \u0434\u0443\u0431\u043b\u044e\u0432\u0430\u043d\u044c.",
            importTitle: "\u0406\u043c\u043f\u043e\u0440\u0442 \u0437 \u0440\u0435\u043f\u043e\u0437\u0438\u0442\u0430\u0440\u0456\u044e",
            importText: "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u043d\u0438\u0439 \u0456\u043c\u043f\u043e\u0440\u0442 \u043c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u0438\u0445 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0439 \u0437 \u0440\u0435\u043f\u043e\u0437\u0438\u0442\u0430\u0440\u0456\u044e \u0443\u043d\u0456\u0432\u0435\u0440\u0441\u0438\u0442\u0435\u0442\u0443 \u0437\u0430 \u043a\u0430\u0444\u0435\u0434\u0440\u0430\u043c\u0438.",
            duplicatesTitle: "\u041f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u0430 \u0434\u0443\u0431\u043b\u0456\u043a\u0430\u0442\u0456\u0432",
            duplicatesText: "\u0410\u043d\u0430\u043b\u0456\u0437 \u0437\u0430\u043f\u0438\u0441\u0456\u0432 \u0434\u043b\u044f \u0432\u0438\u044f\u0432\u043b\u0435\u043d\u043d\u044f \u043e\u0434\u043d\u0430\u043a\u043e\u0432\u0438\u0445 \u043c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u0438\u0445 \u043c\u0430\u0442\u0435\u0440\u0456\u0430\u043b\u0456\u0432.",
            reportsTitle: "\u0424\u043e\u0440\u043c\u0443\u0432\u0430\u043d\u043d\u044f \u0437\u0432\u0456\u0442\u0443",
            reportsText: "\u0413\u0435\u043d\u0435\u0440\u0430\u0446\u0456\u044f Word-\u0437\u0432\u0456\u0442\u0443 \u043d\u0430 \u043e\u0441\u043d\u043e\u0432\u0456 \u043e\u0431\u0440\u0430\u043d\u043e\u0433\u043e \u0448\u0430\u0431\u043b\u043e\u043d\u0443 \u0442\u0430 \u0437\u0456\u0431\u0440\u0430\u043d\u0438\u0445 \u0434\u0430\u043d\u0438\u0445 \u043f\u0440\u043e \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457.",
            createAction: "\u0421\u0442\u0432\u043e\u0440\u0438\u0442\u0438",
            openAction: "\u0412\u0456\u0434\u043a\u0440\u0438\u0442\u0438",
            checkAction: "\u041f\u0435\u0440\u0435\u0432\u0456\u0440\u0438\u0442\u0438",
            generateAction: "\u0417\u0433\u0435\u043d\u0435\u0440\u0443\u0432\u0430\u0442\u0438",
            footer: "\u00a9 2026 \u041a\u0430\u0444\u0435\u0434\u0440\u0430 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043d\u043e\u0457 \u0456\u043d\u0436\u0435\u043d\u0435\u0440\u0456\u0457 \u0442\u0430 \u0456\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0456\u0439\u043d\u0438\u0445 \u0442\u0435\u0445\u043d\u043e\u043b\u043e\u0433\u0456\u0439 \u0443\u043f\u0440\u0430\u0432\u043b\u0456\u043d\u043d\u044f"
        },
        en: {
            brand: "Methodical Publications Registry",
            navPublications: "All Publications",
            navTemplates: "Templates",
            navReports: "Word Report",
            navAdmin: "Administration",
            back: "Back",
            logout: "Log out",
            heroText: "A platform for collecting, checking, importing, and generating reports for university teachers' methodical publications.",
            sectionTitle: "Core Features",
            sectionText: "Choose the section you need to work with publications, templates, or reports.",
            addTitle: "Add Publication",
            addText: "Manually add a methodical publication with format validation and duplicate checks.",
            importTitle: "Repository Import",
            importText: "Automatically import methodical publications from the university repository by department.",
            duplicatesTitle: "Duplicate Check",
            duplicatesText: "Analyze records to find identical methodical materials.",
            reportsTitle: "Report Generation",
            reportsText: "Generate a Word report from the selected template and collected publication data.",
            createAction: "Create",
            openAction: "Open",
            checkAction: "Check",
            generateAction: "Generate",
            footer: "\u00a9 2026 Department of Software Engineering and Management Information Technologies"
        }
    };

    const phraseTranslations = new Map(Object.entries({
        "\u0412\u0445\u0456\u0434": "Login",
        "\u0412\u0445\u0456\u0434 \u0434\u043e \u0441\u0438\u0441\u0442\u0435\u043c\u0438": "Sign in",
        "\u041d\u0435\u0432\u0456\u0440\u043d\u0438\u0439 \u043b\u043e\u0433\u0456\u043d \u0430\u0431\u043e \u043f\u0430\u0440\u043e\u043b\u044c.": "Invalid username or password.",
        "\u0412\u0430\u0448 \u043e\u0431\u043b\u0456\u043a\u043e\u0432\u0438\u0439 \u0437\u0430\u043f\u0438\u0441 \u0449\u0435 \u043d\u0435 \u043f\u0456\u0434\u0442\u0432\u0435\u0440\u0434\u0436\u0435\u043d\u043e \u0430\u0434\u043c\u0456\u043d\u0456\u0441\u0442\u0440\u0430\u0442\u043e\u0440\u043e\u043c.": "Your account has not been approved by an administrator yet.",
        "\u0420\u0435\u0454\u0441\u0442\u0440\u0430\u0446\u0456\u044e \u043d\u0430\u0434\u0456\u0441\u043b\u0430\u043d\u043e. \u041e\u0447\u0456\u043a\u0443\u0439\u0442\u0435 \u043f\u0456\u0434\u0442\u0432\u0435\u0440\u0434\u0436\u0435\u043d\u043d\u044f \u0430\u0434\u043c\u0456\u043d\u0456\u0441\u0442\u0440\u0430\u0442\u043e\u0440\u0430.": "Registration has been submitted. Wait for administrator approval.",
        "\u0412\u0438 \u0432\u0438\u0439\u0448\u043b\u0438 \u0456\u0437 \u0441\u0438\u0441\u0442\u0435\u043c\u0438.": "You have been logged out.",
        "\u041b\u043e\u0433\u0456\u043d": "Username",
        "\u041f\u0430\u0440\u043e\u043b\u044c": "Password",
        "\u0423\u0432\u0456\u0439\u0442\u0438": "Sign in",
        "\u0421\u0442\u0432\u043e\u0440\u0438\u0442\u0438 \u043e\u0431\u043b\u0456\u043a\u043e\u0432\u0438\u0439 \u0437\u0430\u043f\u0438\u0441": "Create an account",
        "\u0420\u0435\u0454\u0441\u0442\u0440\u0430\u0446\u0456\u044f": "Registration",
        "\u0420\u0435\u0454\u0441\u0442\u0440\u0430\u0446\u0456\u044f \u0432\u0438\u043a\u043b\u0430\u0434\u0430\u0447\u0430": "Teacher Registration",
        "\u041f\u0440\u0456\u0437\u0432\u0438\u0449\u0435": "Last name",
        "\u0406\u043c'\u044f": "First name",
        "\u041d\u0430\u0434\u0456\u0441\u043b\u0430\u0442\u0438 \u0437\u0430\u044f\u0432\u043a\u0443": "Submit request",
        "\u0423\u0436\u0435 \u043c\u0430\u0454\u0442\u0435 \u043e\u0431\u043b\u0456\u043a\u043e\u0432\u0438\u0439 \u0437\u0430\u043f\u0438\u0441?": "Already have an account?",
        "\u041f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457": "Publications",
        "\u041c\u0435\u0442\u043e\u0434\u0438\u0447\u043d\u0456 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457": "Methodical Publications",
        "\u0414\u043e\u0434\u0430\u0442\u0438": "Add",
        "\u041d\u0430\u0437\u0430\u0434": "Back",
        "\u041d\u0430 \u0433\u043e\u043b\u043e\u0432\u043d\u0443": "Home",
        "\u0413\u043e\u043b\u043e\u0432\u043d\u0430": "Home",
        "\u0424\u0456\u043b\u044c\u0442\u0440\u0430\u0446\u0456\u044f \u0442\u0430 \u043f\u043e\u0448\u0443\u043a": "Filtering and Search",
        "\u041f\u043e\u0448\u0443\u043a \u0437\u0430 \u043d\u0430\u0437\u0432\u043e\u044e": "Search by title",
        "\u041d\u0430\u0437\u0432\u0430 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457": "Publication title",
        "\u041f\u043e\u0448\u0443\u043a \u0437\u0430 \u0430\u0432\u0442\u043e\u0440\u043e\u043c": "Search by author",
        "\u041f\u0440\u0456\u0437\u0432\u0438\u0449\u0435 \u0430\u0431\u043e \u0456\u043c'\u044f \u0430\u0432\u0442\u043e\u0440\u0430": "Author last name or first name",
        "\u0420\u0456\u043a": "Year",
        "\u0423\u0441\u0456 \u0440\u043e\u043a\u0438": "All years",
        "\u041a\u0430\u0444\u0435\u0434\u0440\u0430": "Department",
        "\u0423\u0441\u0456 \u043a\u0430\u0444\u0435\u0434\u0440\u0438": "All departments",
        "\u0421\u043e\u0440\u0442\u0443\u0432\u0430\u043d\u043d\u044f": "Sorting",
        "\u041d\u043e\u0432\u0456 \u0441\u043f\u043e\u0447\u0430\u0442\u043a\u0443": "Newest first",
        "\u0421\u0442\u0430\u0440\u0456 \u0441\u043f\u043e\u0447\u0430\u0442\u043a\u0443": "Oldest first",
        "\u041d\u0430\u0437\u0432\u0430 \u0410-\u042f": "Title A-Z",
        "\u041d\u0430\u0437\u0432\u0430 \u042f-\u0410": "Title Z-A",
        "\u0417\u0430\u0441\u0442\u043e\u0441\u0443\u0432\u0430\u0442\u0438": "Apply",
        "\u041e\u0447\u0438\u0441\u0442\u0438": "Clear",
        "\u0423 \u0431\u0430\u0437\u0456 \u0434\u0430\u043d\u0438\u0445 \u043f\u043e\u043a\u0438 \u0449\u043e \u043d\u0435\u043c\u0430\u0454 \u0436\u043e\u0434\u043d\u043e\u0457 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u0457.": "There are no publications in the database yet.",
        "\u041d\u0430\u0437\u0432\u0430": "Title",
        "\u0412\u0438\u0445\u0456\u0434\u043d\u0456 \u0432\u0456\u0434\u043e\u043c\u043e\u0441\u0442\u0456": "Publication details",
        "\u041a-\u0441\u0442\u044c \u0441\u0442\u043e\u0440\u0456\u043d\u043e\u043a": "Pages",
        "\u0410\u0432\u0442\u043e\u0440\u0438": "Authors",
        "\u0412\u0438\u0434\u0430\u0432\u043d\u0438\u0446\u0442\u0432\u043e": "Publisher",
        "\u0414\u0436\u0435\u0440\u0435\u043b\u043e": "Source",
        "\u0414\u0456\u0457": "Actions",
        "\u0412\u0456\u0434\u043a\u0440\u0438\u0442\u0438": "Open",
        "\u041d\u0435 \u0432\u043a\u0430\u0437\u0430\u043d\u043e": "Not specified",
        "\u0414\u043e\u0434\u0430\u0442\u0438 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044e": "Add Publication",
        "\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u0442\u0438 \u043f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044e": "Edit Publication",
        "\u0412\u0438\u0434\u0430\u0432\u0435\u0446\u044c": "Publisher",
        "\u041f\u043e\u0441\u0438\u043b\u0430\u043d\u043d\u044f": "Link",
        "\u041e\u0431\u0435\u0440\u0456\u0442\u044c \u0434\u0436\u0435\u0440\u0435\u043b\u043e": "Select source",
        "\u041a\u0456\u043b\u044c\u043a\u0456\u0441\u0442\u044c \u0441\u0442\u043e\u0440\u0456\u043d\u043e\u043a": "Number of pages",
        "\u0411\u0456\u0431\u043b\u0456\u043e\u0433\u0440\u0430\u0444\u0456\u0447\u043d\u0438\u0439 \u043e\u043f\u0438\u0441": "Bibliographic description",
        "\u041a\u043e\u0436\u0435\u043d \u0430\u0432\u0442\u043e\u0440 \u0437 \u043d\u043e\u0432\u043e\u0433\u043e \u0440\u044f\u0434\u043a\u0430": "One author per line",
        "\u0417\u0431\u0435\u0440\u0435\u0433\u0442\u0438": "Save",
        "\u041f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u0430 \u0434\u0443\u0431\u043b\u0456\u043a\u0430\u0442\u0456\u0432": "Duplicate Check",
        "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438 \u043f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u0438": "Check Results",
        "\u041f\u0456\u0434\u043e\u0437\u0440\u0456\u043b\u0438\u0445 \u0434\u0443\u0431\u043b\u0456\u043a\u0430\u0442\u0456\u0432 \u043d\u0435 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043e.": "No suspicious duplicates were found.",
        "\u0417\u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043f\u043e\u0442\u0435\u043d\u0446\u0456\u0439\u043d\u0456 \u0434\u0443\u0431\u043b\u0456\u043a\u0430\u0442\u0438:": "Potential duplicates found:",
        "\u0420\u0456\u0432\u0435\u043d\u044c": "Level",
        "\u041f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044f 1": "Publication 1",
        "\u041f\u0443\u0431\u043b\u0456\u043a\u0430\u0446\u0456\u044f 2": "Publication 2",
        "\u041f\u0456\u0434\u0441\u0443\u043c\u043a\u043e\u0432\u0438\u0439 score": "Final score",
        "\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u0442\u0438 1": "Edit 1",
        "\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u0442\u0438 2": "Edit 2",
        "\u0417\u0432\u0456\u0442\u0438": "Reports",
        "\u0413\u0435\u043d\u0435\u0440\u0430\u0446\u0456\u044f \u0437\u0432\u0456\u0442\u0456\u0432": "Report Generation",
        "\u041e\u0431\u0440\u0430\u0442\u0438 \u0448\u0430\u0431\u043b\u043e\u043d": "Choose Template",
        "\u041d\u0435\u043c\u0430\u0454 \u0448\u0430\u0431\u043b\u043e\u043d\u0456\u0432": "No templates",
        "\u0417\u0433\u0435\u043d\u0435\u0440\u043e\u0432\u0430\u043d\u0456 \u0437\u0432\u0456\u0442\u0438": "Generated Reports",
        "\u041d\u0435\u043c\u0430\u0454 \u0437\u0432\u0456\u0442\u0456\u0432": "No reports",
        "\u0417\u0430\u0432\u0430\u043d\u0442\u0430\u0436\u0438\u0442\u0438": "Download",
        "\u0412\u0438\u0434\u0430\u043b\u0438\u0442\u0438": "Delete",
        "\u0413\u0435\u043d\u0435\u0440\u0430\u0446\u0456\u044f \u0437\u0432\u0456\u0442\u0443": "Report Generation",
        "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u0438 \u0437\u0432\u0456\u0442\u0443": "Report Parameters",
        "\u041d\u0430\u0437\u0430\u0434 \u0434\u043e \u0437\u0432\u0456\u0442\u0456\u0432": "Back to reports",
        "\u041e\u0431\u0435\u0440\u0456\u0442\u044c \u043a\u0430\u0444\u0435\u0434\u0440\u0438": "Choose departments",
        "\u0423 \u0437\u0430\u043f\u0438\u0441\u0430\u0445 \u043f\u043e\u043a\u0438 \u043d\u0435\u043c\u0430\u0454 \u043a\u0430\u0444\u0435\u0434\u0440.": "There are no departments in records yet.",
        "\u041e\u0431\u0435\u0440\u0456\u0442\u044c \u0440\u043e\u043a\u0438": "Choose years",
        "\u0423 \u0437\u0430\u043f\u0438\u0441\u0430\u0445 \u043f\u043e\u043a\u0438 \u043d\u0435\u043c\u0430\u0454 \u0440\u043e\u043a\u0456\u0432.": "There are no years in records yet.",
        "\u041e\u0431\u0435\u0440\u0456\u0442\u044c \u0434\u0436\u0435\u0440\u0435\u043b\u0430": "Choose sources",
        "\u0421\u0444\u043e\u0440\u043c\u0443\u0432\u0430\u0442\u0438 \u0437\u0432\u0456\u0442": "Generate report",
        "\u0413\u043e\u0442\u043e\u0432\u043e": "Ready",
        "\u0417\u0432\u0456\u0442 \u0443\u0441\u043f\u0456\u0448\u043d\u043e \u0441\u0442\u0432\u043e\u0440\u0435\u043d\u043e": "Report created successfully",
        "\u0428\u0430\u0431\u043b\u043e\u043d\u0438 \u0437\u0432\u0456\u0442\u0456\u0432": "Report Templates",
        "\u0414\u043e\u0434\u0430\u0442\u0438 \u0448\u0430\u0431\u043b\u043e\u043d": "Add Template",
        "\u0428\u0430\u0431\u043b\u043e\u043d\u0438 \u0449\u0435 \u043d\u0435 \u0434\u043e\u0434\u0430\u043d\u0456.": "No templates have been added yet.",
        "\u041e\u043f\u0438\u0441": "Description",
        "\u0422\u0438\u043f": "Type",
        "\u0414\u0430\u0442\u0430 \u0441\u0442\u0432\u043e\u0440\u0435\u043d\u043d\u044f": "Created at",
        "\u0412\u0438\u043a\u043e\u0440\u0438\u0441\u0442\u0430\u0442\u0438": "Use",
        "\u0414\u043e\u0434\u0430\u0442\u0438 \u0448\u0430\u0431\u043b\u043e\u043d \u0437\u0432\u0456\u0442\u0443": "Add Report Template",
        "\u041d\u0430\u0437\u0432\u0430 \u0448\u0430\u0431\u043b\u043e\u043d\u0443": "Template name",
        "\u0424\u0430\u0439\u043b \u0448\u0430\u0431\u043b\u043e\u043d\u0443": "Template file",
        "\u0417\u0430\u0432\u0430\u043d\u0442\u0430\u0436\u0438\u0442\u0438 \u0448\u0430\u0431\u043b\u043e\u043d": "Upload template",
        "\u0421\u0438\u043d\u0445\u0440\u043e\u043d\u0456\u0437\u0430\u0446\u0456\u044f \u0437 DSpace": "DSpace Synchronization",
        "\u0406\u043c\u043f\u043e\u0440\u0442 \u0443\u0441\u0456\u0445 \u043a\u0430\u0444\u0435\u0434\u0440": "Import all departments",
        "\u041f\u043e\u0447\u0438\u043d\u0430\u044e\u0447\u0438 \u0437 \u0440\u043e\u043a\u0443": "Starting from year",
        "\u0421\u0438\u043d\u0445\u0440\u043e\u043d\u0456\u0437\u0443\u0432\u0430\u0442\u0438 \u0432\u0441\u0456 \u043a\u0430\u0444\u0435\u0434\u0440\u0438": "Synchronize all departments",
        "\u0406\u043c\u043f\u043e\u0440\u0442 \u043e\u043a\u0440\u0435\u043c\u043e\u0457 \u043a\u0430\u0444\u0435\u0434\u0440\u0438": "Import one department",
        "\u041f\u043e\u0448\u0443\u043a \u043a\u0430\u0444\u0435\u0434\u0440\u0438 \u0437\u0430 \u043d\u0430\u0437\u0432\u043e\u044e": "Search department by name",
        "\u0412\u0432\u0435\u0434\u0456\u0442\u044c \u043d\u0430\u0437\u0432\u0443 \u043a\u0430\u0444\u0435\u0434\u0440\u0438...": "Enter department name...",
        "\u041d\u0435\u043c\u0430\u0454 \u0430\u043a\u0442\u0438\u0432\u043d\u0438\u0445 \u043a\u0430\u0444\u0435\u0434\u0440 \u0443 \u043a\u043e\u043d\u0444\u0456\u0433\u0443\u0440\u0430\u0446\u0456\u0457.": "There are no active departments in the configuration.",
        "\u0421\u0438\u043d\u0445\u0440\u043e\u043d\u0456\u0437\u0443\u0432\u0430\u0442\u0438 \u043a\u0430\u0444\u0435\u0434\u0440\u0443": "Synchronize department",
        "\u041a\u0430\u0444\u0435\u0434\u0440\u0443 \u0437\u0430 \u0442\u0430\u043a\u0438\u043c \u0437\u0430\u043f\u0438\u0442\u043e\u043c \u043d\u0435 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043e.": "No department was found for this query.",
        "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442 \u0441\u0438\u043d\u0445\u0440\u043e\u043d\u0456\u0437\u0430\u0446\u0456\u0457 \u043a\u0430\u0444\u0435\u0434\u0440\u0438": "Department synchronization result",
        "\u041e\u043f\u0440\u0430\u0446\u044c\u043e\u0432\u0430\u043d\u043e:": "Processed:",
        "\u0421\u0442\u0432\u043e\u0440\u0435\u043d\u043e:": "Created:",
        "\u041e\u043d\u043e\u0432\u043b\u0435\u043d\u043e:": "Updated:",
        "\u041f\u0440\u043e\u043f\u0443\u0449\u0435\u043d\u043e:": "Skipped:",
        "\u041f\u043e\u043c\u0438\u043b\u043a\u0438:": "Errors:",
        "\u041f\u0430\u043d\u0435\u043b\u044c \u0430\u0434\u043c\u0456\u043d\u0456\u0441\u0442\u0440\u0430\u0442\u043e\u0440\u0430": "Administrator Panel",
        "\u0417\u0430\u044f\u0432\u043a\u0438 \u043d\u0430 \u0440\u0435\u0454\u0441\u0442\u0440\u0430\u0446\u0456\u044e": "Registration Requests",
        "\u041d\u0435\u043c\u0430\u0454 \u0437\u0430\u044f\u0432\u043e\u043a \u043d\u0430 \u0440\u0435\u0454\u0441\u0442\u0440\u0430\u0446\u0456\u044e.": "There are no registration requests.",
        "\u0414\u0430\u0442\u0430 \u0437\u0430\u044f\u0432\u043a\u0438": "Request date",
        "\u041f\u0456\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u0438": "Approve",
        "\u0412\u0456\u0434\u0445\u0438\u043b\u0438\u0442\u0438": "Reject",
        "\u0410\u043a\u0442\u0438\u0432\u043d\u0456 \u0432\u0438\u043a\u043b\u0430\u0434\u0430\u0447\u0456": "Active Teachers",
        "\u0410\u043a\u0442\u0438\u0432\u043d\u0438\u0445 \u0432\u0438\u043a\u043b\u0430\u0434\u0430\u0447\u0456\u0432 \u043f\u043e\u043a\u0438 \u043d\u0435\u043c\u0430\u0454.": "There are no active teachers yet.",
        "\u0421\u0442\u0430\u0442\u0443\u0441": "Status",
        "\u0423\u0441\u0456 \u043e\u0431\u043b\u0456\u043a\u043e\u0432\u0456 \u0437\u0430\u043f\u0438\u0441\u0438": "All Accounts",
        "\u0420\u043e\u043b\u044c": "Role",
        "\u0417\u043c\u0456\u043d\u0438\u0442\u0438 \u0441\u0442\u0430\u0442\u0443\u0441": "Change status",
        "\u0414\u0456\u044f": "Action"
    }));

    const originalTextNodes = new WeakMap();
    let originalTitle = null;
    const translatableAttributes = ["placeholder", "title", "aria-label"];

    function addStyles() {
        if (document.getElementById("unimethod-language-style")) {
            return;
        }

        const style = document.createElement("style");
        style.id = "unimethod-language-style";
        style.textContent = `
            .language-switch {
                display: inline-flex;
                gap: 2px;
                padding: 2px;
                background-color: rgba(255, 255, 255, 0.18);
                border: 1px solid rgba(255, 255, 255, 0.3);
                border-radius: 999px;
            }
            .language-switch button {
                min-width: 42px;
                border: 0;
                border-radius: 999px;
                padding: 5px 10px;
                background: transparent;
                color: #ffffff;
                font-size: 0.82rem;
                font-weight: 700;
                line-height: 1.1;
            }
            .language-switch button.active {
                background-color: #ffffff;
                color: #163b6b;
            }
            .language-switch-fixed {
                position: fixed;
                top: 12px;
                right: 12px;
                z-index: 1100;
                background-color: #1f4e8c;
                border-color: rgba(255, 255, 255, 0.4);
                box-shadow: 0 8px 22px rgba(0, 0, 0, 0.16);
            }
            html[lang="en"] .cell-fill .expand-note::after,
            html[lang="en"] .expandable-cell:not(.expanded) .expand-hint::after {
                content: "Show full text";
            }
            html[lang="en"] .cell-fill.expanded .expand-note::after,
            html[lang="en"] .expandable-cell.expanded .expand-hint::after {
                content: "Collapse";
            }
        `;
        document.head.appendChild(style);
    }

    function ensureSwitch() {
        if (document.querySelector("[data-lang-option]")) {
            return;
        }

        const switcher = document.createElement("div");
        switcher.className = "language-switch language-switch-fixed";
        switcher.setAttribute("role", "group");
        switcher.setAttribute("aria-label", "Language switch");
        switcher.innerHTML = `
            <button type="button" data-lang-option="uk" aria-pressed="false">UA</button>
            <button type="button" data-lang-option="en" aria-pressed="false">EN</button>
        `;
        document.body.appendChild(switcher);
    }

    function normalize(value) {
        return value.replace(/\s+/g, " ").trim();
    }

    function translateValue(value, language) {
        if (language === "uk") {
            return value;
        }

        const trimmed = normalize(value);
        if (!trimmed) {
            return value;
        }

        const exact = phraseTranslations.get(trimmed);
        if (exact) {
            return value.replace(trimmed, exact);
        }

        const withoutPrefix = trimmed.replace(/^[^\p{L}\p{N}]+/u, "").trim();
        const translatedWithoutPrefix = phraseTranslations.get(withoutPrefix);
        if (translatedWithoutPrefix) {
            return value.replace(withoutPrefix, translatedWithoutPrefix);
        }

        return value;
    }

    function shouldSkipNode(node) {
        const parent = node.parentElement;
        return !parent
            || parent.closest("script, style, [data-i18n], [data-lang-option], .language-switch");
    }

    function translateTextNodes(language) {
        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
        const nodes = [];

        while (walker.nextNode()) {
            nodes.push(walker.currentNode);
        }

        nodes.forEach((node) => {
            if (shouldSkipNode(node)) {
                return;
            }

            if (!originalTextNodes.has(node)) {
                originalTextNodes.set(node, node.textContent);
            }

            const original = originalTextNodes.get(node);
            node.textContent = language === "uk" ? original : translateValue(original, language);
        });
    }

    function translateAttributes(language) {
        document.querySelectorAll("*").forEach((element) => {
            translatableAttributes.forEach((attribute) => {
                if (!element.hasAttribute(attribute)) {
                    return;
                }

                const originalAttribute = `data-language-original-${attribute}`;
                if (!element.hasAttribute(originalAttribute)) {
                    element.setAttribute(originalAttribute, element.getAttribute(attribute));
                }

                const original = element.getAttribute(originalAttribute);
                element.setAttribute(attribute, language === "uk" ? original : translateValue(original, language));
            });
        });
    }

    function translateKeyedElements(language) {
        const dictionary = keyedTranslations[language] || keyedTranslations.uk;
        document.querySelectorAll("[data-i18n]").forEach((element) => {
            const key = element.dataset.i18n;
            if (dictionary[key]) {
                element.textContent = dictionary[key];
            }
        });
    }

    function translateDocumentTitle(language) {
        if (originalTitle === null) {
            originalTitle = document.title;
        }

        document.title = language === "uk" ? originalTitle : translateValue(originalTitle, language);
    }

    function updateButtons(language) {
        document.querySelectorAll("[data-lang-option]").forEach((button) => {
            const isActive = button.dataset.langOption === language;
            button.classList.toggle("active", isActive);
            button.setAttribute("aria-pressed", String(isActive));
        });
    }

    function applyLanguage(language) {
        const selectedLanguage = language === "en" ? "en" : "uk";

        document.documentElement.lang = selectedLanguage;
        translateDocumentTitle(selectedLanguage);
        translateKeyedElements(selectedLanguage);
        translateTextNodes(selectedLanguage);
        translateAttributes(selectedLanguage);
        updateButtons(selectedLanguage);
        localStorage.setItem(storageKey, selectedLanguage);
    }

    function bindSwitch() {
        document.querySelectorAll("[data-lang-option]").forEach((button) => {
            button.addEventListener("click", () => applyLanguage(button.dataset.langOption));
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        addStyles();
        ensureSwitch();
        bindSwitch();
        applyLanguage(localStorage.getItem(storageKey) || "uk");
    });
})();

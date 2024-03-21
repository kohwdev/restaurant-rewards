const email = document.getElementById("username");

email.addEventListener("input", (event) => {
    const enteredEmail = event.target.value;
    const emailRegex =
        /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if (!emailRegex.test(enteredEmail)) {
        // If the entered email does not match the expected format, add the 'invalid-email' class
        email.classList.add("invalid-email");
    } else {
        // If the entered email matches the expected format, remove the 'invalid-email' class
        email.classList.remove("invalid-email");
    }
});

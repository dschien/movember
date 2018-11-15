# Movember YASTA (Yet Another Spring Tutorial App)

## Structure

### Spring MVC

#### Basic Controller  
```java
    @GetMapping("/")
    public String index() {
        return "index";
    }

```

This most simple controller just serves the name of a static template without any dynamic content. FYI - there is a shortcut 
via the `ViewControllerRegistry` method :
```java
public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/contact");
        registry.addViewController("/").setViewName("index");
```  

#### A Level-Up Controller - 
Moving on, we want to have controller serves a template that show dynamic content, i.e. things that change. In our case
the list of beards we have stored in DB:
```java
@GetMapping("/beard")
    public String showBeardForm(Beard beard, Model model) {
        model.addAttribute("beards", beardRepository.findAll());
        return "beard";
    }
```  

This queries our repository and hands all beards in there to the model that provides the data to be rendered in the context 
of the template.

This is basically
```html
<head>

</head>
<body>

<list of beards>
</list>

<form to add new beard>
</form>
</body>
```

Here is said template in full glory. 
```html
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="UTF-8">
    <title>Movember Beard Names</title>
    <style>
        .text-success   {color: red;}
        .help-block    {color: green;}
    </style>
</head>
<body>


<div>

    <h1>Known Beards</h1>

    <table class="table">
        <thead>
        <td>Id</td>
        <td>Name</td>

        </thead>
        <tr th:each="beard: ${beards}">
            <td><span th:text="${beard.id}"/></td>
            <td><span th:text="${beard.name}"/></td>
        </tr>
    </table>

    <h1>Add your beard name.</h1>
    <form th:action="@{/beard}" th:object="${beard}" method="post" class="form">
        <p th:text="${message}" class="text-success"></p>
        <label for="name">Name</label>
        <input type="text" id="name" name="name"/>
        <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}" class="help-block"></span>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
<a href="/">Back home</a>
</body>
</html>
```  

The first thing to look at is
```html
<table class="table">
        <thead>
        <td>Id</td>
        <td>Name</td>

        </thead>
        <tr th:each="beard: ${beards}">
            <td><span th:text="${beard.id}"/></td>
            <td><span th:text="${beard.name}"/></td>
        </tr>
    </table>
```
This iterates over all beards in `beards` and fills the table row by row.

##### Form
The second block is the form. 
Now check out [this section in Santa's Grotto README](https://github.com/dschien/santas-grotto#a-form-with-spring-step-by-step).
It follows the same principles.

The only difference is that same page that renders the form template also includes a list of DB content. 
Hence we can redirect to that page after successfully adding a beard - last line below
```java
    @PostMapping(value = "/beard")
    public String submitContact(@Valid Beard beard, BindingResult binding, RedirectAttributes attr) {
        if (binding.hasErrors()) {
            return "/beard";
        }
        beardRepository.save(beard);
        attr.addFlashAttribute("message", "Thank you for your beard.");
        return "redirect:/beard";
    }

``` 

The binding related aspects have been used in Santa's Grotto already. Importantly, your form template needs to 
have a `th:object` with the name of a parameter to bind to

So, here
```html
<form th:action="@{/beard}" th:object="${beard}" method="post" class="form">
``` 
`beard` binds to `Beard beard` in 
```java
@GetMapping("/beard")
    public String showBeardForm(Beard beard, Model model) {
```
*To repeat* you need to provide an instance of a Beard with the get handler. Thymeleaf is using it in the background to 
and then passes it to the post handler as the `beard` parameter.  
```java
@PostMapping(value = "/beard")
public String submitContact(@Valid Beard beard, BindingResult binding, RedirectAttributes attr) {
```

That beard needs to pass the checks we have defined on the `Entity`, in particular
```java
@NotEmpty
String name;
``` 

If we do not provide a name - binding errors will result and passed back to the form where they are handled by 
`<span th:if="${#fields.hasErrors('name')}" th:errors="*{name}" class="help-block"></span>`

Note the `th:if` - which only renders an HTML element if `true`. 
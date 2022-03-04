    #type vertex
    #version 330 core
    layout (location=0) in vec3 aPos; // a for attribute
    layout (location=1) in vec4 aColor;// a for attribute

    uniform mat4 uProjectionMatrix;
    uniform mat4 uViewMatrix;

    out vec4 fColor; // f for fragment (fragment shader)

    void main(){
        fColor = aColor;
        gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0); // create vector 4 using aPos as the first 3 parts of the vector
    }

    #type fragment
    #version 330 core

    in vec4 fColor; // if we have an out, we need an in

    out vec4 color; // color GL is outputting

    void main(){
        color = fColor;
    }




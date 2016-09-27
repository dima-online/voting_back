package kz.bsbnb.cli;

import kz.bsbnb.BlockChainApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.CommandLine;
import org.springframework.shell.core.JLineShellComponent;

/**
 * Created by Timur.Abdykarimov on 03.09.2016.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {"kz.bsbnb",
                "org.springframework.shell.commands",
                "org.springframework.shell.converters",
                "org.springframework.shell.plugin.support"})
@Import(BlockChainApplication.class)
public class StartCLI {
    public static void main( String ... args ) {
        ConfigurableApplicationContext ctx =
                new SpringApplicationBuilder( StartCLI.class ).run( args );

        JLineShellComponent shell = ctx.getBean( JLineShellComponent.class );
        shell.setPrintBanner( true );
        shell.start();
        shell.promptLoop();
        shell.waitForComplete();
        ctx.close();
    }
    // is necessary, autowired in:
    // org.springframework.shell.converters.AvailableCommandsConverter
    @Bean( name = "shell" )
    public JLineShellComponent jLineShellComponent() {
        return new JLineShellComponent();
    }

    // is necessary, autowired in:
    // org.springframework.shell.core.JLineShellComponent
    @Bean
    public CommandLine commandLine() {
        return new CommandLine( null, 3000, null );
    }
}
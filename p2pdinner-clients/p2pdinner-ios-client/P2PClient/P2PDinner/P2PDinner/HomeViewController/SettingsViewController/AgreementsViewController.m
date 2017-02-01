//
//  AgreementsViewController.m
//  P2PDinner
//
//  Created by Selvam M on 5/31/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import "AgreementsViewController.h"
#import "ActivityView.h"
#import "StringConstants.h"
@interface AgreementsViewController ()<UIWebViewDelegate>{
 ActivityView *activityView;
}
@end

@implementation AgreementsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:_pdfURL];
    _pdfWebView.delegate = self;
    [_pdfWebView loadRequest:requestObj];

    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)webViewDidStartLoad:(UIWebView *)webView{
    [activityView startAnimating:kLoadingText];
    [self.view addSubview:activityView];

}
- (void)webViewDidFinishLoad:(UIWebView *)webView{
        if (activityView.isAnimating) {
            [activityView stopAnimating];
            [activityView removeFromSuperview];
        }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
